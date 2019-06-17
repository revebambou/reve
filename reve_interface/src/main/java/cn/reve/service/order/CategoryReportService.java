package cn.reve.service.order;

import cn.reve.pojo.order.CategoryReport;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CategoryReportService {

    List<CategoryReport> countCategoryReport(LocalDate date);

    String autoRespawnCategoryReport(LocalDate localDate);

    List<Map> countCategoryReportByDate(String dateBefore, String dateAfter);
}
