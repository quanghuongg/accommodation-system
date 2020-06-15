package com.accommodation.system.service.impl;

import com.accommodation.system.service.AmazonS3Service;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * User: huongnq4
 * Date:  15/06/2020
 * Time: 17 :38
 * To change this template use File | Settings | File and Code Templates.
 */
@Service
public class AmazonS3ServiceImpl implements AmazonS3Service {
    @Autowired
    AmazonS3 s3client;

    @Override
    public void uploadFile(String path, File file) {
        s3client.putObject(
                "huongnq",
                path,
                file
        );
    }
}
