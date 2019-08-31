package in.gohelper.rest;

import in.gohelper.models.CustomerAddressmodels.CustomerAddressModel;
import in.gohelper.models.CustomerAddressmodels.CustomerAddressUpdateModel;
import in.gohelper.models.MarketingBoxModels.MarketingBoxModel;
import in.gohelper.models.SuccessModel;
import in.gohelper.models.cartmodels.CartModel;
import in.gohelper.models.cartmodels.SuccessCartModel;
import in.gohelper.models.customerprofilemodels.CustomerProfileModel;
import in.gohelper.models.emergencyservicemodels.EmergencyServiceModel;
import in.gohelper.models.guestmodels.GuestIdModel;
import in.gohelper.models.ordermodels.OrdersModel;
import in.gohelper.models.otpmodels.OtpModel;
import in.gohelper.models.otpverifymodels.OtpVerifyModel;
import in.gohelper.models.paymentmodels.PaymentOptionsModel;
import in.gohelper.models.servicemodels.ServiceDetailModel;
import in.gohelper.models.servicemodels.ServiceModel;
import in.gohelper.models.staticpagesmodels.StaticPagesModel;
import in.gohelper.models.testimonialsmodels.TestimonialsModel;
import in.gohelper.urls.Urls;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET(Urls.GUEST_ID_URL)
    Call<GuestIdModel> getGuestId(@Header("x-device-id") String deviceId);

    @GET(Urls.GET_OTP_URL)
    Call<OtpModel> getOtp(@Header("x-guest-id") String guestId, @Query("mobile") String mobileNumber);

    @GET(Urls.GET_OTP_URL)
    Call<OtpVerifyModel> verifyOtp(@Header("x-guest-id") String guestId, @Query("mobile") String mobile, @Query("code") String code);

    @GET(Urls.CUSTOMER_URL)
    Call<CustomerProfileModel> customerProfile(@Header("Authorization") String authTokenType, @Path("id") int customerId);

    @FormUrlEncoded
    @PUT(Urls.CUSTOMER_URL)
    Call<CustomerProfileModel> updateProfile(@Header("Authorization") String authTokenType,
                                             @Path("id") int customerId,
                                             @Field("first_name") String firstName,
                                             @Field("last_name") String lastName,
                                             @Field("email") String email,
                                             @Field("profession") String profession,
                                             @Field("social_token") String socialToken,
                                             @Field("social_user_id") String socialUserId,
                                             @Field("social_site") String socialSite);

    @GET(Urls.MARKETING_BOX_URL)
    Call<MarketingBoxModel> marketingBox(@Path("identifier") String campaignIdentifier);

    @GET(Urls.EMERGENCY_SERVICE_URL)
    Call<EmergencyServiceModel> emergencyServices();

    @GET(Urls.TEXTIMONIAIL)
    Call<TestimonialsModel> testimonial();

    @FormUrlEncoded
    @POST(Urls.CUSTOMER_ADDRESS)
    Call<CustomerAddressUpdateModel> customerAddress(@Header("Authorization") String authTokenType,
                                                     @Field("full_name") String fullName,
                                                     @Field("phone_number") String phone,
                                                     @Field("locality") String locality,
                                                     @Field("address") String address,
                                                     @Field("landmark") String landmark,
                                                     @Field("pincode") String pincode);

    @GET(Urls.CUSTOMER_ADDRESS)
    Call<CustomerAddressModel> getCustomerAddress(@Header("Authorization") String authTokenType);

    @FormUrlEncoded
    @PUT(Urls.URL_CUSTOMER_ADDRESS)
    Call<CustomerAddressUpdateModel> updateCustomerAddress(@Header("Authorization") String authTokenType,
                                                           @Path("id") int customerAddressId,
                                                           @Field("full_name") String fullName,
                                                           @Field("phone_number") String phone,
                                                           @Field("locality") String locality,
                                                           @Field("address") String address,
                                                           @Field("landmark") String landmark,
                                                           @Field("pincode") String pincode);

    @DELETE(Urls.URL_CUSTOMER_ADDRESS)
    Call<CustomerAddressUpdateModel> deleteCustomerAddress(@Header("Authorization") String authTokenType, @Path("id") int customerAddressId);


    @GET(Urls.TOP_SERVICES_URL)
    Call<ServiceModel> getTopService();

    @GET(Urls.SERVICE_DETAILS_URL)
    Call<ServiceDetailModel> getServiceDetail(@Header("Authorization") String authTokenType, @Path("id") int serviceId);

    @GET(Urls.AUTO_COMPLETE_TEXT_URL)
    Call<ServiceModel> searchService(@Query("q") String query);

    @FormUrlEncoded
    @POST(Urls.CART_URL)
    Call<SuccessModel> createCart(@Header("Authorization") String authTokenType,
                                  @Field("service") long serviceId,
                                  @Field("address") long addressId,
                                  @Field("scheduled_time") String scheduledTime,
                                  @Field("options") String options,
                                  @Field("slot") String slot,
                                  @Field("item_amount") String itemAmount);


    @GET(Urls.CART_URL)
    Call<CartModel> getCart(@Header("Authorization") String authTokenType);

    @FormUrlEncoded
    @PUT(Urls.PUT_DELETE_CART_URL)
    Call<SuccessModel> updateCart(@Header("Authorization") String authTokenType,
                                  @Path("id") int cartItemId,
                                  @Field("service") long serviceId,
                                  @Field("address") long addressId,
                                  @Field("scheduled_time") String scheduledTime,
                                  @Field("options") String options,
                                  @Field("slot") String slot);


    @DELETE(Urls.PUT_DELETE_CART_URL)
    Call<SuccessCartModel> deleteCart(@Header("Authorization") String authTokenType, @Path("id") int cartId);


    @GET(Urls.PAYMENT_OPTIONS_URL)
    Call<PaymentOptionsModel> getPaymentOptions(@Header("Authorization") String authTokenType);

    @FormUrlEncoded
    @POST(Urls.PAYMENT_OPTIONS_URL)
    Call<SuccessModel> createCODPayment(@Header("Authorization") String authTokenType,
                                        @Field("payment_identifier") String paymentIdentifier,
                                        @Field("total_amount") double amount);

    @GET(Urls.GET_ORDER_URL)
    Call<OrdersModel> getOrders(@Header("Authorization") String authTokenType);

    @DELETE(Urls.CANCEL_ORDER_URL)
    Call<SuccessModel> cancelOrder(@Header("Authorization") String authTokenType, @Path("id") int orderId);

    @GET(Urls.GET_STATIC_PAGES)
    Call<StaticPagesModel> getStaticPages();

    @FormUrlEncoded
    @PUT(Urls.GET_CONTACT_US)
    Call<SuccessModel> getContactUs(@Field("page_id") int pageId,
                                    @Field("full_name") String fullName,
                                    @Field("phone_number") String phone,
                                    @Field("email") String email,
                                    @Field("query_type") String queryType,
                                    @Field("query_text") String queryText);

    @FormUrlEncoded
    @PUT(Urls.CUSTOMER_UPDATE_LOCATION)
    Call<SuccessModel> updateCustomerLocation(@Header("x-guest-id") String guestId,
                                    @Header("x-customer-id") long CustomerId,
                                    @Field("address") String address,
                                    @Field("pincode") String pinCode);
}

