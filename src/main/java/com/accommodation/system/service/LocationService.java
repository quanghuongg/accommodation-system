package com.accommodation.system.service;

import com.accommodation.system.entity.District;
import com.accommodation.system.entity.RoomType;
import com.accommodation.system.entity.Ward;

import java.util.List;

public interface LocationService {
    List<District> listDistrict();

    District findDistrict(int id);

    List<Ward> listWard(int districtId);

    List<RoomType> listRoomType();

    Ward findWard(int id);
}
