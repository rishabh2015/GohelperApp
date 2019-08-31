
package in.gohelper.models.paymentmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentOptionsModel {

    @SerializedName("data")
    @Expose
    private List<Payment> data = null;

    public List<Payment> getData() {
        return data;
    }

    public void setData(List<Payment> data) {
        this.data = data;
    }

}
