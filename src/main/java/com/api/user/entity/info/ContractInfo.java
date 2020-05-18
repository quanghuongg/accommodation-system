package com.api.user.entity.info;

import com.api.user.entity.Contract;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContractInfo {

    @JsonProperty(value = "student_name")
    private String studentName;

    @JsonProperty(value = "tutor_name")
    private String tutorName;

    private int number_hour;

    private long date_from;

    private long date_to;

    private long created;

    private long updated;

    private double total;

    private int status;

    private String description;

    private String skill;

    private int tutor_id;

    private int student_id;

    public ContractInfo(Contract contract) {
        this.number_hour = contract.getNumber_hour();
        this.date_from = contract.getDate_from();
        this.date_to = contract.getDate_to();
        this.created = contract.getCreated();
        this.updated = contract.getUpdated();
        this.total = contract.getTotal();
        this.status = contract.getStatus();
        this.description = contract.getDescription();
    }
}
