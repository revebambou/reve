package cn.reve.dao;

import cn.reve.pojo.order.CategoryReport;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CategoryReportMapper extends Mapper<CategoryReport> {

    @Select("SELECT category_id1 categoryId1, category_id2 categoryId2, category_id3 categoryId3, DATE_FORMAT(o.`pay_time`, '%Y-%m-%d') countDate, SUM(num) num, SUM(money) money " +
            "FROM tb_order_item oi, tb_order o " +
            "WHERE oi.order_id = o.id AND pay_status = 1 AND is_delete = 0 AND DATE_FORMAT(o.`pay_time`, '%Y-%m-%d') = #{date} " +
            "GROUP BY category_id1, category_id2, category_id3, DATE_FORMAT(o.`pay_time`, '%Y-%m-%d')")
    public List<CategoryReport> countCategoryReport(@Param("date") LocalDate date);


    @Select("SELECT category_id1 categoryId1, NAME categoryName, count_date countDate, SUM(num) num, SUM(money) money " +
            "FROM tb_category_report cr, v_category c " +
            "WHERE cr.category_id1 = c.id AND count_date<=#{dateAfter} AND count_date>=#{dateBefore} " +
            "GROUP BY category_id1")
    //incorrect sql syntax, group is typed as order
    //why the return type using List<Map> instead of List<CategoryReport>???  done...
    public List<Map> countCategoryReportByDate(@Param("dateBefore") String dateBefore, @Param("dateAfter") String dateAfter);
}
