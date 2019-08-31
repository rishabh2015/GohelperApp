
package in.gohelper.models.cartmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartData {

    @SerializedName("cart_item")
    @Expose
    private CartItem cartItem;

    public CartItem getCartItem() {
        return cartItem;
    }

    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
    }

}
