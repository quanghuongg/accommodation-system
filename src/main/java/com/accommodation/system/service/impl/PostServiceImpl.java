package com.accommodation.system.service.impl;

import com.accommodation.system.dao.PostDao;
import com.accommodation.system.define.Constant;
import com.accommodation.system.entity.Post;
import com.accommodation.system.entity.model.SearchResult;
import com.accommodation.system.entity.request.PostRequest;
import com.accommodation.system.entity.request.SearchInput;
import com.accommodation.system.mapper.DistrictMapper;
import com.accommodation.system.mapper.PostMapper;
import com.accommodation.system.mapper.WardMapper;
import com.accommodation.system.service.PostService;
import com.google.api.client.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service(value = "postService")
public class PostServiceImpl implements PostService {
    @Autowired
    PostMapper postMapper;

    @Autowired
    PostDao postDao;

    @Autowired
    DistrictMapper districtMapper;

    @Autowired
    WardMapper wardMapper;

    @Override
    public String createPost(Integer userId, PostRequest postRequest) {
        StringBuilder location = new StringBuilder();
        location.append(postRequest.getLocation());
        location.append(", ");
        if (postRequest.getWardId() > 0) {
            location.append(wardMapper.findWard(postRequest.getWardId()).getName());
            location.append(", ");
        }
        if (postRequest.getDistrictId() > 0) {
            location.append(districtMapper.findDistrict(postRequest.getDistrictId()).getName());
            location.append(", ");
        }
        location.append("TP. Hồ Chí Minh");


        Post post = Post.builder()
                .description(postRequest.getDescription())
                .createdAt(System.currentTimeMillis())
                .isVerified(0)
                .location(location.toString())
                .price(postRequest.getPrice())
                .roomTypeId(postRequest.getRoomTypeId())
                .userId(userId)
                .districtId(postRequest.getDistrictId())
                .wardId(postRequest.getWardId())
                .area(postRequest.getArea())
                .build();
        return postDao.createPost(post);
    }

    @Override
    public Post findPost(String postId) throws IOException {
        Post post = postDao.find(postId);
        //load image
        try (Stream<Path> walk = Files.walk(Paths.get( Constant.FileUploader.PATH_IMAGES + "/" + postId))) {
            List<String> result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());
            List<File> images = new ArrayList<>();
            for (String path : result) {
                File file = new File(path);
                images.add(file);
            }
            post.setImages(images);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public SearchResult loadByIds(SearchInput requestInput) throws IOException {
        return postDao.loadByIds(requestInput);
    }


    @Override
    public SearchResult doSearch(SearchInput searchInput) throws IOException {
        return postDao.loadBySearchRequest(searchInput);
    }
}
