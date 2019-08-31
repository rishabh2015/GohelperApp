package in.gohelper.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessModel {
    @SerializedName("success")
    @Expose
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
