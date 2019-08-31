
package in.gohelper.models.MarketingBoxModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MarketingBoxModel {

    @SerializedName("data")
    @Expose
    private List<MarketingBoxData> data = null;

    public List<MarketingBoxData> getData() {
        return data;
    }

    public void setData(List<MarketingBoxData> data) {
        this.data = data;
    }

}
