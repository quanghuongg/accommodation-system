package com.accommodation.system.entity.model;

import com.accommodation.system.entity.base.BaseModel;
import com.accommodation.system.uitls.jackson.CustomDateDeSerializer;
import com.accommodation.system.uitls.jackson.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.ColumnDefault;


import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationTokenInfo extends BaseModel {

    int userId;

    private String deviceId;

    private String deviceToken;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeSerializer.class)
    private Date createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeSerializer.class)
    private Date updatedAt;
    public NotificationTokenInfo(int userId, String deviceId, String deviceToken) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.deviceToken = deviceToken;
    }
    public boolean hasDeviceId() {
        return deviceId != null && StringUtils.isNotEmpty(deviceId.trim());
    }

    public boolean hasDeviceToken() {
        return deviceToken != null && StringUtils.isNotEmpty(deviceToken.trim());
    }
}
