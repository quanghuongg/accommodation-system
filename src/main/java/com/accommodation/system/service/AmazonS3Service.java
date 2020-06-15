package com.accommodation.system.service;

import java.io.File;

public interface AmazonS3Service {

    void uploadFile(String path, File file);
}
