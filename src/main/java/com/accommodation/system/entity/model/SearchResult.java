package com.accommodation.system.entity.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class SearchResult {
    @JsonProperty("total")
    long total = 0;

    @JsonProperty("count")
    int count = 0;

    @JsonProperty("hits")
    List hits = new LinkedList();

    @JsonProperty("info")
    private Object info;
}
