package in.gohelper.interfaces;

import in.gohelper.models.ordermodels.OrderData;

public interface OrderActionListener {
    void onDeleteOrderItem(OrderData orderItem);
}
