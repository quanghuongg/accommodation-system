package com.accommodation.system.entity.response;

import com.accommodation.system.entity.Contract;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class ListContract {
    @JsonProperty("contracts")
    private List<Contract> contracts;

    @JsonProperty("total")
    private int total;

    @JsonProperty("page")
    private int page;

    @JsonProperty("size")
    private int size;
}
