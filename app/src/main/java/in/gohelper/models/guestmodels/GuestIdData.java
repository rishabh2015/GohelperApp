
package in.gohelper.models.guestmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GuestIdData {

    @SerializedName("guest_id")
    @Expose
    private String guestId;

    /**
     * 
     * @return
     *     The guestId
     */
    public String getGuestId() {
        return guestId;
    }

    /**
     * 
     * @param guestId
     *     The guest_id
     */
    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

}
