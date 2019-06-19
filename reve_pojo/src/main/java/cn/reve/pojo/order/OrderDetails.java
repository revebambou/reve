package cn.reve.pojo.order;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

public class OrderDetails implements Serializable {

    private Order order;
    private List<OrderItem> orderItemList;

    public OrderDetails(Order order, List<OrderItem> orderItemList) {
        this.order = order;
        this.orderItemList = orderItemList;
    }

    public OrderDetails() {
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "order=" + order +
                ", orderItemList=" + orderItemList +
                '}';
    }
}
