package com.accommodation.system.entity.info;

import com.accommodation.system.entity.Contract;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Builder
public class RevenueInfo {
    private List<Contract> list;

    double total;
}
