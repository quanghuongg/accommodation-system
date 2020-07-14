package com.accommodation.system.config;

import com.accommodation.system.define.Constant;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * User: huongnq4
 * Date:  15/06/2020
 * Time: 17 :35
 * To change this template use File | Settings | File and Code Templates.
 */
@Configuration
public class AmazonS3Config {

    @Bean
    public AmazonS3 initAmazonS3() {
        AWSCredentials credentials = new BasicAWSCredentials(
                Constant.ACCESS_KEY,
                Constant.SECRET_KEY
        );

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.AP_SOUTHEAST_1)
                .build();
    }
}
