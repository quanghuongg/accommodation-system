package com.accommodation.system.service;

import java.io.File;
import java.util.List;

public interface AmazonS3Service {

    void uploadFile(String path, File file);

    List<String> listFileImages(String postId);

}
