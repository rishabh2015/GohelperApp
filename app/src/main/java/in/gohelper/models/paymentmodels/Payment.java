
package in.gohelper.models.paymentmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payment {

    @SerializedName("payment")
    @Expose
    private PaymentData payment;

    public PaymentData getPayment() {
        return payment;
    }

    public void setPayment(PaymentData payment) {
        this.payment = payment;
    }

}
