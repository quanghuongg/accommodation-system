package com.accommodation.system.entity.model;

import com.accommodation.system.define.Constant;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocialAccountInfo {

    @JsonProperty(value = Constant.SocialAccountInfo.JsonField.ID_TOKEN)
    private String idToken;

    @JsonProperty(value = Constant.SocialAccountInfo.JsonField.TYPE)
    private String type;

    @JsonProperty(value = Constant.SocialAccountInfo.JsonField.ROLE_ID)
    private int roleId;
}
