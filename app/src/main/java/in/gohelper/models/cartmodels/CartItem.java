
package in.gohelper.models.cartmodels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import in.gohelper.models.CustomerAddressmodels.CustomerAddressData;
import in.gohelper.models.servicemodels.ServiceData;

public class CartItem {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("service_options")
    @Expose
    private String serviceOptions;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("scheduled_date_time")
    @Expose
    private String scheduledDateTime;
    @SerializedName("slot_start")
    @Expose
    private String slotStart;
    @SerializedName("slot_end")
    @Expose
    private String slotEnd;
    @SerializedName("item_amount")
    @Expose
    private String itemAmount;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("service")
    @Expose
    private ServiceData service;
    @SerializedName("address")
    @Expose
    private CustomerAddressData address;
    @SerializedName("cart")
    @Expose
    private Cart cart;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceOptions() {
        return serviceOptions;
    }

    public void setServiceOptions(String serviceOptions) {
        this.serviceOptions = serviceOptions;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

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

    public String getSlotEnd() {
        return slotEnd;
    }

    public void setSlotEnd(String slotEnd) {
        this.slotEnd = slotEnd;
    }

    public String getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(String itemAmount) {
        this.itemAmount = itemAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
