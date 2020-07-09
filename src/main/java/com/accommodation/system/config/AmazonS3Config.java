package com.accommodation.system.config;

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
                "AKIA2DIUBXATOSND4UJS",
                "QbE/m91mNOei8X6+9z2M78ljZMH7IFDCXTNedsp/"
        );
//                AWSCredentials credentials = new BasicAWSCredentials(
//                "AKIA5FTVWSYGWC6KQPPV",
//                "yIKbzqNwhlv74PT5qYNeVvMKjvsRbsj9r2SVCqQL"
//        );
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.AP_SOUTHEAST_1)
                .build();
    }
}
