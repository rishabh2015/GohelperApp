
package in.gohelper.models.ordermodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("order_no")
    @Expose
    private String orderNo;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("order_time")
    @Expose
    private String orderTime;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("order_items")
    @Expose
    private List<OrderItem> orderItems = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

}
