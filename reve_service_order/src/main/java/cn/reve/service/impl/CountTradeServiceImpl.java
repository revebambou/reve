package cn.reve.service.impl;

import cn.reve.dao.OrderMapper;
import cn.reve.pojo.order.CountTrade;
import cn.reve.service.order.CountTradeService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CountTradeServiceImpl implements CountTradeService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public boolean autoSpawnCountTrade(String date) {

        CountTrade countTrade = new CountTrade();
        //Get persons of booking orders, this requires user functionality, cannot be done yet, set 100 person as default num
        int personsOrdered = orderMapper.getPersonsOrdered(date);
        personsOrdered = initNum(personsOrdered, 100);
        System.out.println(personsOrdered);
        return false;
    }

    private int initNum(int origin, int initValue){
        return origin==0 ? initValue:origin;
    }
}
