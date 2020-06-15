package com.accommodation.system.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import java.io.File;
import java.util.List;

/**
 * User: huongnq4
 * Date:  15/06/2020
 * Time: 16 :54
 * To change this template use File | Settings | File and Code Templates.
 */
public class MyAmazonS3 {
    static AWSCredentials credentials = new BasicAWSCredentials(
            "AKIA2DIUBXATOSND4UJS",
            "QbE/m91mNOei8X6+9z2M78ljZMH7IFDCXTNedsp/"
    );
    static AmazonS3 s3client = AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.AP_SOUTHEAST_1)
            .build();




    public static void main(String[] args) {

        s3client.putObject(
                "huongnq",
                "Document/hello.txt",
                new File("/home/huongnq/Documents/LuanVan/accommodation-system/data/images/6eebec01-07ac-4e23-8e8a-ff58a6f83003/c2e5abad-dc2c-42cc-9c46-b5bf52ba8c5d.jpg")
        );

        ObjectListing listing = s3client.listObjects( "huongnq", "Document" );
        List<S3ObjectSummary> summaries = listing.getObjectSummaries();

        while (listing.isTruncated()) {
            listing = s3client.listNextBatchOfObjects (listing);
            summaries.addAll (listing.getObjectSummaries());
        }
    }



}
