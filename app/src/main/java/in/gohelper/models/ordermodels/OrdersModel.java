
package in.gohelper.models.ordermodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrdersModel {

    @SerializedName("data")
    @Expose
    private List<OrderData> data = null;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public List<OrderData> getData() {
        return data;
    }

    public void setData(List<OrderData> data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

}
