
package in.gohelper.models.otpverifymodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtpVerifyData {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("auth_token")
    @Expose
    private String authToken;
    @SerializedName("auth_token_type")
    @Expose
    private String authTokenType;
    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("is_new_customer")
    @Expose
    private String customerName;


    /**
     * 
     * @return
     *     The success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * 
     * @param success
     *     The success
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     * 
     * @return
     *     The authToken
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * 
     * @param authToken
     *     The auth_token
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    /**
     * 
     * @return
     *     The authTokenType
     */
    public String getAuthTokenType() {
        return authTokenType;
    }

    /**
     * 
     * @param authTokenType
     *     The auth_token_type
     */
    public void setAuthTokenType(String authTokenType) {
        this.authTokenType = authTokenType;
    }

    /**
     * 
     * @return
     *     The customerId
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * 
     * @param customerId
     *     The customer_id
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     *
     * @return
     *     The customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     *
     * @param name
     *     The customerName
     */
    public void setNewCustomer(String name) {
        customerName = name;
    }

}
