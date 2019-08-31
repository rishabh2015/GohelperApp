
package in.gohelper.models.emergencyservicemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmergencyServiceModel {

    @SerializedName("data")
    @Expose
    private List<EmergencyServiceData> data = null;

    public List<EmergencyServiceData> getData() {
        return data;
    }

    public void setData(List<EmergencyServiceData> data) {
        this.data = data;
    }

}
