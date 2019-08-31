
package in.gohelper.models.guestmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GuestIdModel {

    @SerializedName("data")
    @Expose
    private GuestIdData data;

    /**
     * 
     * @return
     *     The data
     */
    public GuestIdData getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(GuestIdData data) {
        this.data = data;
    }

}
