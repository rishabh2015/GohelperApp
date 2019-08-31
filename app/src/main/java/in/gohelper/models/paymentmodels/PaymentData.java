
package in.gohelper.models.paymentmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("payment_identifier")
    @Expose
    private String paymentIdentifier;
    @SerializedName("payment_label")
    @Expose
    private String paymentLabel;
    @SerializedName("payment_description")
    @Expose
    private String paymentDescription;
    @SerializedName("payment_url")
    @Expose
    private String paymentUrl;
    @SerializedName("payment_callback")
    @Expose
    private String paymentCallback;
    @SerializedName("sort_order")
    @Expose
    private String sortOrder;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPaymentIdentifier() {
        return paymentIdentifier;
    }

    public void setPaymentIdentifier(String paymentIdentifier) {
        this.paymentIdentifier = paymentIdentifier;
    }

    public String getPaymentLabel() {
        return paymentLabel;
    }

    public void setPaymentLabel(String paymentLabel) {
        this.paymentLabel = paymentLabel;
    }

    public String getPaymentDescription() {
        return paymentDescription;
    }

    public void setPaymentDescription(String paymentDescription) {
        this.paymentDescription = paymentDescription;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getPaymentCallback() {
        return paymentCallback;
    }

    public void setPaymentCallback(String paymentCallback) {
        this.paymentCallback = paymentCallback;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
