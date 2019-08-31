
package in.gohelper.models.customerprofilemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerProfileModel {

    @SerializedName("data")
    @Expose
    private CustomerProfileData data;

    public CustomerProfileData getData() {
        return data;
    }

    public void setData(CustomerProfileData data) {
        this.data = data;
    }

}
