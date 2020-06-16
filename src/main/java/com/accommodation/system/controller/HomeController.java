package com.accommodation.system.controller;

import com.accommodation.system.define.Constant;
import com.accommodation.system.define.ContextPath;
import com.accommodation.system.entity.model.Response;
import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = {ContextPath.Home.HOME})
public class HomeController {
    @Autowired
    LocationService locationService;

    @RequestMapping(value = {ContextPath.Home.DISTRICTS}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> listDistrict() {
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .data(locationService.listDistrict())
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = {ContextPath.Home.WARDS}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> listDistrict(@RequestParam("district_id") int districtId) throws ApiServiceException {
        if (districtId < 1) {
            throw new ApiServiceException("district_id must not be null");
        }
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .data(locationService.listWard(districtId))
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
