package com.accommodation.system;

import com.accommodation.system.entity.Post;
import com.accommodation.system.uitls.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * User: huongnq4
 * Date:  02/07/2020
 * Time: 10 :31
 * To change this template use File | Settings | File and Code Templates.
 */
public class Test {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T[] subArray(T[] array, int beg, int end) {
        return Arrays.copyOfRange(array, beg, end + 1);
    }


    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("responseUpdate.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray companyList = (JSONArray) jsonObject.get("data");
            for (Object object : companyList) {
                Map post = objectMapper.readValue(JsonUtil.toJsonString(object), Map.class);
                Post entity = new Post();
                entity.setArea((Integer) post.get("area"));
                entity.setCreatedAt(System.currentTimeMillis());
                entity.setLocation(post.get("location").toString());
                entity.setDescription(post.get("description").toString());
                entity.setUserId((Integer) post.get("user_id"));
                entity.setPrice((Integer) post.get("price"));
                int random_int = (int) (Math.random() * (5 - 1 + 1) + 1);
                entity.setRoomTypeId(random_int);
                entity.setTitle(post.get("title").toString());
                entity.setWardId(random_int);
                entity.setDistrictId(1);
                ArrayList<String> image = (ArrayList<String>) post.get("images");
                String[] array = image.toArray(new String[image.size()]);
                if (array.length > 5) {
                    array = subArray(array, 0, 4);
                }
                Map fullWard = (Map)post.get("full_address_object");
                Map wardinfo = (Map) fullWard.get("ward");
                entity.setImages(array);
                entity.setImage(array[0]);
                System.out.println(JsonUtil.toJsonString(post));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
