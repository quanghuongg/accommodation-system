package com.api.user.entity.info;

import com.api.user.entity.Contract;
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
