package com.accommodation.system.service.impl;

import com.accommodation.system.entity.District;
import com.accommodation.system.entity.RoomType;
import com.accommodation.system.entity.Ward;
import com.accommodation.system.mapper.DistrictMapper;
import com.accommodation.system.mapper.RoomTypeMapper;
import com.accommodation.system.mapper.WardMapper;
import com.accommodation.system.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: huongnq4
 * Date:  15/06/2020
 * Time: 10 :37
 * To change this template use File | Settings | File and Code Templates.
 */
@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    DistrictMapper districtMapper;
    @Autowired
    WardMapper wardMapper;

    @Autowired
    RoomTypeMapper roomTypeMapper;

    @Override
    public List<District> listDistrict() {
        return districtMapper.listDistrict();
    }

    @Override
    public District findDistrict(int id) {
        return districtMapper.findDistrict(id);
    }

    @Override
    public List<Ward> listWard(int districtId) {
        return wardMapper.listWard(districtId);
    }

    @Override
    public Ward findWard(int id) {
        return wardMapper.findWard(id);
    }

    @Override
    public List<RoomType> listRoomType() {
        return roomTypeMapper.listRoomType();
    }
}
