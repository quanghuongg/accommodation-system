package com.accommodation.system.controller;

import com.accommodation.system.dao.PostDao;
import com.accommodation.system.define.Constant;
import com.accommodation.system.entity.Post;
import com.accommodation.system.entity.Ward;
import com.accommodation.system.entity.model.Response;
import com.accommodation.system.mapper.WardMapper;
import com.accommodation.system.uitls.JsonUtil;
import com.accommodation.system.uitls.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * User: huongnq4
 * Date:  02/07/2020
 * Time: 11 :37
 * To change this template use File | Settings | File and Code Templates.
 */
@RestController
public class DevController {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T[] subArray(T[] array, int beg, int end) {
        return Arrays.copyOfRange(array, beg, end + 1);
    }

    @Autowired
    PostDao postDao;

    @Autowired
    WardMapper wardMapper;

    @RequestMapping(value = {"/fake"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> fakeData() {
        JSONParser parser = new JSONParser();
        int count = 0;
        try {
            Object obj = parser.parse(new FileReader("quanbt.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray companyList = (JSONArray) jsonObject.get("data");

            for (Object object : companyList) {
                Map post = objectMapper.readValue(JsonUtil.toJsonString(object), Map.class);
                Post entity = new Post();
                entity.setArea((int) (Math.random() * (40 - 1 + 1) + 1));
                entity.setCreatedAt(System.currentTimeMillis());
                entity.setLocation(post.get("exact_room_address").toString());
                entity.setDescription(post.get("notes").toString());
//                entity.setUserId((Integer) post.get("user_id"));
                entity.setUserId((int) (Math.random() * (20 - 1 + 1) + 1));
                entity.setPrice((Integer) post.get("room_price"));
                int rom = (int) (Math.random() * (5 - 1 + 1) + 1);
                entity.setRoomTypeId(rom);
                entity.setTitle(post.get("room_name").toString());

                Map fullWard = (Map) post.get("full_address_object");
                Map wardinfo = (Map) fullWard.get("ward");
                Ward ward = wardMapper.findWardByName(wardinfo.get("text").toString().trim(), 14);
                if (Utils.isNotEmpty(ward)) {
                    entity.setWardId(ward.getId());
                } else {
                    int x = (int) (Math.random() * (191 - 172 + 1) + 172);
                    entity.setWardId(x);
                }
                entity.setDistrictId(14);
                ArrayList<String> image = (ArrayList<String>) post.get("upload_room_images");
                String[] array = image.toArray(new String[image.size()]);
                if (array.length > 5) {
                    array = subArray(array, 0, 4);
                }
                entity.setImages(array);
                entity.setImage(array[0]);
                entity.setStatus(1);
                postDao.createPost(entity);
                count++;
                if(count==100)
                {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .data(count)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
