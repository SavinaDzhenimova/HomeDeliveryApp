package com.homedelivery.model.user;

import com.homedelivery.model.enums.UpdateInfo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserUpdateInfoDTO {

    @NotNull(message = "{user_update_property_not_null}")
    private UpdateInfo updateInfo;

    @NotNull
    @Size(min = 3, message = "{user_update_property_data_size}")
    private String data;

    public UserUpdateInfoDTO() {
    }

    public UpdateInfo getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(UpdateInfo updateInfo) {
        this.updateInfo = updateInfo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
