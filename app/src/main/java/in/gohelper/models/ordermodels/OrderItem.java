
package in.gohelper.models.ordermodels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import in.gohelper.models.CustomerAddressmodels.CustomerAddressData;
import in.gohelper.models.servicemodels.ServiceData;

public class OrderItem {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("order_status_id")
    @Expose
    private String orderStatusId;
    @SerializedName("service_options")
    @Expose
    private String serviceOptions;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("order_no")
    @Expose
    private String orderNo;

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String order) {
        this.orderTime = order;
    }

    private String orderTime;

    public String getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(String scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }

    public String getSlotStart() {
        return slotStart;
    }

    public void setSlotStart(String slotStart) {
        this.slotStart = slotStart;
    }

    public String getSloEnd() {
        return sloEnd;
    }

    public void setSloEnd(String sloEnd) {
        this.sloEnd = sloEnd;
    }

    @SerializedName("scheduled_date_time")
    @Expose
    private String scheduledDateTime;
    @SerializedName("slot_start")
    @Expose
    private String slotStart;
    @SerializedName("slot_end")
    @Expose
    private String sloEnd;

    @SerializedName("service")
    @Expose
    private ServiceData service;
    @SerializedName("address")
    @Expose
    private CustomerAddressData address;
    @SerializedName("status")
    @Expose
    private Status status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(String orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public String getServiceOptions() {
        return serviceOptions;
    }

    public void setServiceOptions(String serviceOptions) {
        this.serviceOptions = serviceOptions;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public ServiceData getService() {
        return service;
    }

    public void setService(ServiceData service) {
        this.service = service;
    }

    public CustomerAddressData getAddress() {
        return address;
    }

    public void setAddress(CustomerAddressData address) {
        this.address = address;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
