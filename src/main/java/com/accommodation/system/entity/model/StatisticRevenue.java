package com.accommodation.system.entity.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class StatisticRevenue {
    long date;
    int total;
}