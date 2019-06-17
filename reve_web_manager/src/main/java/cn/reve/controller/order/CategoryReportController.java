package cn.reve.controller.order;

import cn.reve.pojo.order.CategoryReport;
import cn.reve.service.order.CategoryReportService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class CategoryReportController {

    @Reference
    private CategoryReportService categoryReportService;

    @GetMapping("/getCategoryReport")
    public List<Map> getCategoryReport(String dateBefore, String dateAfter) throws ParseException {

        //Convert the format in this place temporary
        System.out.println(dateAfter+"==="+dateBefore);

/*
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(dateBefore);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateBefore = dateFormat.format(date);
        System.out.println(dateBefore);
*/


        List<Map> categoryReportList = categoryReportService.countCategoryReportByDate(dateBefore, dateAfter);
        return categoryReportList;
    }
}
