package cn.reve.service.order;
import cn.reve.entity.PageResult;
import cn.reve.pojo.order.Order;
import cn.reve.pojo.order.OrderDetails;

import java.util.*;

/**
 * order业务逻辑层
 */
public interface OrderService {


    public List<Order> findAll();


    public PageResult<Order> findPage(int page, int size);


    public List<Order> findList(Map<String,Object> searchMap);


    public PageResult<Order> findPage(Map<String,Object> searchMap,int page, int size);


    public Order findById(String id);

    public void add(Order order);


    public void update(Order order);


    public void delete(String id);

    void saveOrderDetails(OrderDetails orderDetails);

    OrderDetails queryOrderDetailById(String orderId);

}
