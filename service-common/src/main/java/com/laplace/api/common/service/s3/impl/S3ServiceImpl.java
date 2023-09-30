package com.laplace.api.common.service.s3.impl;

import static com.laplace.api.common.constants.ApplicationConstants.StringUtils.BACK_SLASH;
import static com.laplace.api.common.constants.ApplicationConstants.StringUtils.UNDER_SCORE;
import static com.laplace.api.common.constants.ApplicationConstants.TEMP_DIR;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.laplace.api.common.configuration.aws.LaplaceAWSConnectionFactory;
import com.laplace.api.common.configuration.aws.LaplaceS3Client;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ApplicationConstants.StringUtils;
import com.laplace.api.common.constants.enums.FileType;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.constants.enums.SupportedImageType;
import com.laplace.api.common.service.s3.LaplaceS3Service;
import com.laplace.api.common.util.BaseResponse;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class S3ServiceImpl implements LaplaceS3Service {

  private final LaplaceAWSConnectionFactory laplaceAWSConnectionFactory;
  private final ObjectMapper objectMapper;
  private AmazonS3 s3Client;
  private LaplaceS3Client laplaceS3Client;
  @Value("${spring.profiles.active}")
  private String activeProfile;

  @Autowired
  public S3ServiceImpl(LaplaceAWSConnectionFactory laplaceAWSConnectionFactory,
      ObjectMapper objectMapper) {
    this.laplaceAWSConnectionFactory = laplaceAWSConnectionFactory;
    this.objectMapper = objectMapper;
  }

  @PostConstruct
  public void initClients() {
    this.laplaceS3Client = laplaceAWSConnectionFactory.getLaplaceS3Client();
    this.s3Client = this.laplaceS3Client.getS3client();
  }

  @Override
  public String uploadMultiPartFile(MultipartFile multipartFile, String accessId,
      FileType fileType) {
    log.debug(String.format("File type: %s", multipartFile.getContentType()));
    File file = convertMultiPartToFile(multipartFile);
    String fileExt = getFileExt(multipartFile);

    file = removeExifMetadata(file, new File("/tmp/out." + fileExt), fileExt);
    return upload(file, accessId, fileType);
  }

  @Override
  public String uploadPublicResources(BaseResponse resources) throws IOException {
    if (laplaceS3Client.getApplicationProfile()
        .equalsIgnoreCase(ApplicationConstants.APPLICATION_LOCAL_PROFILE)) {
      log.debug("Skip resource file upload for {}", laplaceS3Client.getApplicationProfile());
      return ApplicationConstants.StringUtils.EMPTY_STRING;
    } else {
      ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
      File file = getPublicResourceFile();
      writer.writeValue(file, resources);
      return upload(file, null, FileType.PUBLIC_RESOURCE);
    }
  }

  @Override
  public Pair<String, String> generateUploadPresignedUrl(String fileName, String fileContentType) {
    String bucketName = laplaceS3Client.getMediaBucketName();
    Date expiration = new Date();

    expiration.setTime(expiration
        .toInstant()
        .plus(laplaceS3Client.getPresignedUrlExpirationInMinutes(), ChronoUnit.MINUTES)
        .toEpochMilli());

    GeneratePresignedUrlRequest presignedUrlRequest = new GeneratePresignedUrlRequest(bucketName,
        fileName)
        .withExpiration(expiration)
        .withMethod(HttpMethod.PUT)
        .withContentType(fileContentType);

    URL presignedUrl = s3Client.generatePresignedUrl(presignedUrlRequest);
    log.debug("presignedUrl: " + presignedUrl);

    String s3FileUrl = getS3FileUrl(fileName);
    return Pair.of(s3FileUrl, presignedUrl.toString());
  }

  @Override
  public String getS3FileUrl(String fileName) {
    String fileUrl = laplaceS3Client.getCloudFrontURL() + StringUtils.BACK_SLASH + fileName;
    log.info("fileUrl : " + fileUrl);
    return fileUrl;
  }

  @Override
  public String getMediaBucketName() {
    return laplaceS3Client.getMediaBucketName();
  }

  @Override
  public String upload(File file, String fileName, FileType fileType) {
    String bucketName = laplaceS3Client.getMediaBucketName();
    try {
      if (!ApplicationConstants.APPLICATION_LOCAL_PROFILE.equals(activeProfile)) {
        uploadFileToS3bucket(bucketName, fileName, file, fileType);
      }
    } catch (SdkClientException sdkEx) {
      log.error(sdkEx.getMessage());
      throwApplicationException(ResultCodeConstants.FILE_UPLOAD_FAILED);
    } finally {
      try {
        Files.delete(file.toPath());
      } catch (IOException e) {
        log.error("{} not deleted. Message: {}", file.getName(), e.getLocalizedMessage());
      }
    }
    return getS3FileUrl(fileName);
  }

  private File getPublicResourceFile() {
    return new File(TEMP_DIR + BACK_SLASH + laplaceS3Client.getLaplaceResourceFileName());
  }

  private String getFileExt(MultipartFile multipartFile) {
    return null != multipartFile.getContentType()
        && multipartFile.getContentType().split(BACK_SLASH).length > ApplicationConstants.ONE ?
        multipartFile.getContentType().split(BACK_SLASH)[ApplicationConstants.ONE] : "";
  }

  private void uploadFileToS3bucket(String bucketName, String fileName, File file,
      FileType fileType) {
    switch (fileType) {
      case PUBLIC_RESOURCE:
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file)
            .withCannedAcl(CannedAccessControlList.PublicRead));
        break;
      default:
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
    }
  }

  /**
   * Remove image meta data and returns a new image based on image type
   *
   * @param imageFile
   * @param dst
   * @return
   */
  private File removeExifMetadata(final File imageFile, final File dst, String fileExt) {
    if (fileExt.equalsIgnoreCase(SupportedImageType.PNG.name())
        || fileExt.equalsIgnoreCase(SupportedImageType.GIF.name())) {
      try {
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        ImageIO.write(bufferedImage, fileExt, dst);
      } catch (IOException e) {
        log.error("Meta data removal failed!!");
        throwApplicationException(ResultCodeConstants.UNSUPPORTED_IMAGE_TYPE);
      }
    } else {
      try (FileOutputStream fos = new FileOutputStream(dst);
          OutputStream os = new BufferedOutputStream(fos)) {
        new ExifRewriter().removeExifMetadata(imageFile, os);
      } catch (ImageWriteException | IOException | ImageReadException e) {
        log.error("Meta data removal failed: %s" + e.getMessage());
        return imageFile;
      }
    }
    return dst;
  }

  /**
   * Returns a string of format [width]x[height], e.g. 300x600
   *
   * @param file
   * @return
   */
  private String getImageResolution(File file) {
    try {
      ImageInfo imageInfo = Imaging.getImageInfo(file);
      return imageInfo.getWidth() + "x" + imageInfo.getHeight();
    } catch (ImageReadException e) {
      log.error("Invalid image format: %s", e.getMessage());
    } catch (IOException e) {
      log.error("Image resolution I/O exception: %s", e.getMessage());
    }
    return "";
  }

  private File convertMultiPartToFile(MultipartFile file) {
    File convFile = new File(
        TEMP_DIR + BACK_SLASH + Objects.requireNonNull(file.getOriginalFilename()));
    try (FileOutputStream fos = new FileOutputStream(convFile)) {
      fos.write(file.getBytes());
    } catch (IOException ioEx) {
      log.error("Conversion exception: " + ioEx.getMessage());
      throwApplicationException(ResultCodeConstants.MULTI_PART_FILE_CONVERSION_FAILED);
    }
    return convFile;
  }

  /**
   * Generate a string of format [accessId]/[current time in millis]_[file type]_[[width]x[height]]
   * <br> e.g. 1b4d7158-81cd-11e9-a0f3-588a5a46c1e6/1559210305709_profile_637x800.jpg
   *
   * @param file
   * @param accessId
   * @param fileType values: {@link FileType#COVER_IMAGE}, {@link FileType#PROFILE_IMAGE}
   * @return
   */
  private String generateFileName(File file, String accessId, FileType fileType) {
    String fileExtension = FilenameUtils.getExtension(Objects.requireNonNull(file).getName());
    String fileTypeVal = fileType.getFileType();
    switch (fileType) {
      case PROFILE_IMAGE:
      case COVER_IMAGE:
      case SECTION_IMAGE:
        return accessId
            + BACK_SLASH + System.currentTimeMillis()
            + UNDER_SCORE + fileTypeVal
            + UNDER_SCORE + getImageResolution(file)
            + "." + fileExtension;
      case FAV_ICON_IMAGE:
        return accessId
            + BACK_SLASH + System.currentTimeMillis()
            + UNDER_SCORE + fileTypeVal
            + "." + fileExtension;
      case VIDEO:
        return accessId
            + BACK_SLASH + System.currentTimeMillis()
            + UNDER_SCORE + fileTypeVal
            + "." + fileExtension;
      case PUBLIC_RESOURCE:
        return file.getName();
      default:
        return null;
    }
  }
}
