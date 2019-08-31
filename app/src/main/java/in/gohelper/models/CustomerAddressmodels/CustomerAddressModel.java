
package in.gohelper.models.CustomerAddressmodels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomerAddressModel {

    @SerializedName("data")
    @Expose
    private List<CustomerAddressData> data = null;

    public List<CustomerAddressData> getData() {
        return data;
    }

    public void setData(List<CustomerAddressData> data) {
        this.data = data;
    }

}
