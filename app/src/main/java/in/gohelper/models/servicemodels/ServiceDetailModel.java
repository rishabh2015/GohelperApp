
package in.gohelper.models.servicemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceDetailModel {

    @SerializedName("data")
    @Expose
    private ServiceData data = null;

    public ServiceData getData() {
        return data;
    }

    public void setData(ServiceData data) {
        this.data = data;
    }

}
