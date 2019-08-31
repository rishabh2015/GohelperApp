
package in.gohelper.models.cartmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartModel {

    @SerializedName("data")
    @Expose
    private List<CartData> data = null;

    public List<CartData> getData() {
        return data;
    }

    public void setData(List<CartData> data) {
        this.data = data;
    }

}
