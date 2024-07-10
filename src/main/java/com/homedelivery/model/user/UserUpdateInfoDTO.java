package com.homedelivery.model.user;

import com.homedelivery.model.enums.UpdateInfo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserUpdateInfoDTO {

    @NotNull(message = "You must select a property to update!")
    private UpdateInfo updateInfo;

    @NotNull
    @Size(min = 3, message = "Data length must be at least 3 characters!")
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
