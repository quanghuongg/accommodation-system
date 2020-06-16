package com.accommodation.system.service.impl;

import com.accommodation.system.define.Constant;
import com.accommodation.system.service.AmazonS3Service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
                Constant.BUCKET_NAME,
                path,
                file
        );
    }

    @Override
    public List<String> listFileImages(String postId) {
        ObjectListing listing = s3client.listObjects(Constant.BUCKET_NAME, Constant.FileUploader.PATH_IMAGES + "/" + postId.replaceAll("-", ""));
        List<S3ObjectSummary> summaries = listing.getObjectSummaries();
        List<String> list = new ArrayList<>();
        for (S3ObjectSummary objectSummary : summaries) {
            list.add(Constant.HOST_STATIC_WEB + objectSummary.getKey());
        }
        return list;
    }
}
