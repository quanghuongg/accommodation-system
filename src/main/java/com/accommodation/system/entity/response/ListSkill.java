package com.accommodation.system.entity.response;

import com.accommodation.system.entity.Skill;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class ListSkill {
    @JsonProperty("skills")
    private List<Skill> skillList;

    @JsonProperty("total")
    private int total;

    @JsonProperty("page")
    private int page;

    @JsonProperty("size")
    private int size;
}
