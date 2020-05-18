package com.accommodation.system.entity.response;

import com.accommodation.system.entity.Feedback;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class ListFeedback {
    @JsonProperty("feedbacks")
    private List<Feedback> feedbacks;

    @JsonProperty("total")
    private int total;

    @JsonProperty("page")
    private int page;

    @JsonProperty("size")
    private int size;
}
