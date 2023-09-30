package com.laplace.api.file.utils;

import static com.laplace.api.common.util.LaplaceResponseUtil.returnApplicationException;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ApplicationConstants.StringUtils;
import com.laplace.api.common.constants.enums.FileType;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.constants.enums.SupportedImageType;
import com.laplace.api.common.util.DateUtil;
import com.laplace.api.file.payload.response.FileUploadInfo;
import com.laplace.api.file.payload.response.FileUploadPresignedUrlInfo;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public final class FileUtil {

  private static final long KB = 1024;
  private static final long MB = 1024 * KB;
  private static final long GB = 1024 * MB;
  private static final String S3_RESOURCE_PREFIX = "resource" + StringUtils.BACK_SLASH;

  private FileUtil() {
  }

  public static String generateFileName(String activeProfile, FileUploadInfo info) {
    FileType fileType = info.getFileType();
    String accessId = info.getAccessId();

    String fileName = info.getMultipartFile().getOriginalFilename();
    String extension = FilenameUtils.getExtension(fileName);
    if (org.apache.commons.lang3.StringUtils.isBlank(extension)) {
      throw returnApplicationException(ResultCodeConstants.FILE_UPLOAD_FAILED);
    }

    String prefix = S3_RESOURCE_PREFIX + activeProfile + StringUtils.BACK_SLASH + accessId;
    switch (fileType) {
      case PROFILE_IMAGE:
        prefix += "";
        break;

      default:
    }

    return prefix
        + StringUtils.BACK_SLASH + fileType.getFileType()
        + StringUtils.UNDER_SCORE + DateUtil.timeNowToEpochMilli()
        + StringUtils.DOT + extension
        ;
  }

  public static String generateFileName(String activeProfile, FileUploadPresignedUrlInfo info) {
    FileType fileType = info.getFileType();
    String accessId = info.getAccessId();

    String fileName = info.getFileName();
    String extension = FilenameUtils.getExtension(fileName);
    if (org.apache.commons.lang3.StringUtils.isBlank(extension)) {
      throw returnApplicationException(
          ResultCodeConstants.FILE_UPLOAD_PRE_SIGNED_URL_GENERATE_FAILED);
    }

    return S3_RESOURCE_PREFIX + activeProfile
        + StringUtils.BACK_SLASH + fileType.getFileType()
        + StringUtils.BACK_SLASH + accessId
        + StringUtils.BACK_SLASH + DateUtil.timeNowToEpochMilli()
        + StringUtils.DOT + extension
        ;
  }

  public static File convertMultiPartToFile(MultipartFile file) {
    log.debug(String.format("File type: %s", file.getContentType()));
    File convFile = new File(ApplicationConstants.TEMP_DIR
        + StringUtils.BACK_SLASH
        + Objects.requireNonNull(file.getOriginalFilename()));

    try (FileOutputStream fos = new FileOutputStream(convFile)) {
      fos.write(file.getBytes());
    } catch (IOException ioEx) {
      log.error("Conversion exception: " + ioEx.getMessage());
      throwApplicationException(ResultCodeConstants.MULTI_PART_FILE_CONVERSION_FAILED);
    }

    return convFile;
  }

  public static String getFileExt(MultipartFile multipartFile) {
    String contentType = multipartFile.getContentType();
    if (contentType == null) {
      return "";
    }

    String[] split = contentType.split(StringUtils.BACK_SLASH);
    if (split.length == 0) {
      return "";
    }

    return split[ApplicationConstants.ONE];
  }

  /**
   * Remove image meta data and returns a new image based on image type
   */
  public static File removeExifMetadata(final File imageFile, final File dst, String fileExt) {
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

  public static void checkFileSize(FileType fileType, Long fileSize) {
    if (fileSize > (ApplicationConstants.FILE_SIZE * MB)) {
      throwApplicationException(ResultCodeConstants.IMAGE_FILE_SIZE_EXCEEDS);
    }
  }

  public static String getImageResolution(File file) {
    try {
      ImageInfo imageInfo = Imaging.getImageInfo(file);
      return imageInfo.getWidth() + "x" + imageInfo.getHeight();
    } catch (ImageReadException e) {
      log.error("Invalid image format: " + e.getMessage());
    } catch (IOException e) {
      log.error("Image resolution I/O exception: " + e.getMessage());
    }

    return "";
  }
}
