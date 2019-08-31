
package in.gohelper.models.otpverifymodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtpVerifyModel {

    @SerializedName("data")
    @Expose
    private OtpVerifyData data;

    /**
     * 
     * @return
     *     The data
     */
    public OtpVerifyData getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(OtpVerifyData data) {
        this.data = data;
    }

}
