package com.api.user.entity.info;

import com.api.user.define.Constant;
import com.api.user.entity.User;
import com.api.user.uitls.jackson.CustomDateDeSerializer;
import com.api.user.uitls.jackson.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo extends User {

    @JsonProperty(value = Constant.UserInfo.JsonField.USER_ID)
    private long userId;

    @JsonProperty(value = Constant.UserInfo.JsonField.USERNAME)
    private String username;

    @JsonProperty(value = Constant.UserInfo.JsonField.FACEBOOK_UID)
    private String facebookUid;

    @JsonProperty(value = Constant.UserInfo.JsonField.GOOGLE_UID)
    private String googleUid;

    @JsonProperty(value = Constant.UserInfo.JsonField.PASSWORD, access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(value = Constant.UserInfo.JsonField.EMAIL)
    private String email;

    @JsonProperty(value = Constant.UserInfo.JsonField.FULL_NAME)
    private String fullName;

    @JsonProperty(value = Constant.UserInfo.JsonField.CREATED_AT)
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeSerializer.class)
    private Date createdAt;

    @JsonProperty(value = Constant.UserInfo.JsonField.UPDATED_AT)
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeSerializer.class)
    private Date updatedAt;

    @JsonProperty(value = Constant.UserInfo.JsonField.AVATAR)
    private String avatar;

    @JsonProperty(value = Constant.UserInfo.JsonField.ROLE_ID, access = JsonProperty.Access.WRITE_ONLY)
    private int roleId;

    @JsonProperty(value = Constant.UserInfo.JsonField.ID_TOKEN, access = JsonProperty.Access.WRITE_ONLY)
    private String firebaseIdToken;


}
