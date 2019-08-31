
package in.gohelper.models.otpmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtpModel {

    @SerializedName("data")
    @Expose
    private OtpData data;

    /**
     * 
     * @return
     *     The data
     */
    public OtpData getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(OtpData data) {
        this.data = data;
    }
}
