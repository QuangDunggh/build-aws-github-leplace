package com.laplace.api.common.service.s3;

import com.laplace.api.common.constants.enums.FileType;
import com.laplace.api.common.util.BaseResponse;
import java.io.File;
import java.io.IOException;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.multipart.MultipartFile;

public interface LaplaceS3Service {

  /**
   * Upload multi part file in S3 bucket
   *
   * @param multipartFile
   * @param accessId      unique UUID generated for the user
   * @return S3 url to get the object
   */
  String uploadMultiPartFile(MultipartFile multipartFile, String accessId, FileType fileType);

  String uploadPublicResources(BaseResponse resources) throws IOException;

  Pair<String, String> generateUploadPresignedUrl(String fileName, String fileContentType);

  String getS3FileUrl(String fileName);

  String getMediaBucketName();

  String upload(File file, String fileName, FileType fileType);

}
