
package in.gohelper.models.cartmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cart {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("couponCode")
    @Expose
    private Object couponCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(Object couponCode) {
        this.couponCode = couponCode;
    }

}
