
package in.gohelper.models.staticpagesmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StaticPagesModel {

    @SerializedName("data")
    @Expose
    private List<StaticPageData> data = null;

    public List<StaticPageData> getData() {
        return data;
    }

    public void setData(List<StaticPageData> data) {
        this.data = data;
    }

}
