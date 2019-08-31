package in.gohelper.interfaces;


import in.gohelper.models.cartmodels.CartData;

public interface CartActionListener {
    void onDeleteCartItem(CartData cartItem);
    void onEditCartItem(CartData cartItem);
    void onSelectCartItem(CartData cartItem);
}
