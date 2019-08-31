
package in.gohelper.models.servicemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceModel {

    @SerializedName("data")
    @Expose
    private List<ServiceData> data = null;

    public List<ServiceData> getData() {
        return data;
    }

    public void setData(List<ServiceData> data) {
        this.data = data;
    }

}
