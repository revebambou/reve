package cn.reve.service.impl;

import cn.reve.dao.CategoryReportMapper;
import cn.reve.pojo.order.CategoryReport;
import cn.reve.service.order.CategoryReportService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CategoryReportService.class)
public class CategoryReportServiceImpl implements CategoryReportService {

    @Autowired
    private CategoryReportMapper categoryReportMapper;

    @Override
    public List<CategoryReport> countCategoryReport(LocalDate date) {
        System.out.println(date);
        return categoryReportMapper.countCategoryReport(date);
    }

    @Override
    @Transactional
    public String autoRespawnCategoryReport(LocalDate localDate) {
        List<CategoryReport> categoryReportServiceList = categoryReportMapper.countCategoryReport(localDate);
        if(categoryReportServiceList!=null && categoryReportServiceList.size()!=0) {
            for (CategoryReport categoryReport : categoryReportServiceList) {
                categoryReportMapper.insertSelective(categoryReport);
            }
            return "Insert successfully";
        }else{
            String result = "No such data gotten";
            return result;
        }
    }

    @Override //why there cannot be using annotation @Override???
    public List<Map> countCategoryReportByDate(String dateBefore, String dateAfter) {
        return categoryReportMapper.countCategoryReportByDate(dateBefore, dateAfter);
    }
}
