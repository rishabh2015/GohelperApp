
package in.gohelper.models.otpmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtpData {

    @SerializedName("success")
    @Expose
    private Boolean success;

    /**
     * 
     * @return
     *     The success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * 
     * @param success
     *     The success
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

}
