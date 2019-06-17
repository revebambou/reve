package cn.reve.dao;

import cn.reve.pojo.order.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface OrderMapper extends Mapper<Order> {

    @Select("SELECT COUNT(u.username) " +
            "FROM " +
            "(SELECT DISTINCT username " +
            "FROM tb_order " +
            "WHERE DATE_FORMAT(create_time, '%Y-%m-%d')=#{date} " +
            "GROUP BY username ) u")
    int getPersonsOrdered(@Param("date") String date);

}
