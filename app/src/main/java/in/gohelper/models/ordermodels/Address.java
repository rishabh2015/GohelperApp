
package in.gohelper.models.ordermodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("landmark")
    @Expose
    private Object landmark;
    @SerializedName("locality")
    @Expose
    private String locality;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("address_lattitude")
    @Expose
    private Object addressLattitude;
    @SerializedName("address_longitude")
    @Expose
    private Object addressLongitude;
    @SerializedName("is_default")
    @Expose
    private String isDefault;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Object getLandmark() {
        return landmark;
    }

    public void setLandmark(Object landmark) {
        this.landmark = landmark;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Object getAddressLattitude() {
        return addressLattitude;
    }

    public void setAddressLattitude(Object addressLattitude) {
        this.addressLattitude = addressLattitude;
    }

    public Object getAddressLongitude() {
        return addressLongitude;
    }

    public void setAddressLongitude(Object addressLongitude) {
        this.addressLongitude = addressLongitude;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

}
