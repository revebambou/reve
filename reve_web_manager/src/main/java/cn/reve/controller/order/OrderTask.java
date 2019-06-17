package cn.reve.controller.order;

import cn.reve.entity.Result;
import cn.reve.pojo.order.CategoryReport;
import cn.reve.service.order.CategoryReportService;
import cn.reve.service.order.CountTradeService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderTask {

    @Reference
    private CategoryReportService categoryReportService;

    @Reference
    private CountTradeService countTradeService;

    @GetMapping("/countCategoryReport")
    private List<CategoryReport> countCategoryReport(){
        LocalDate localDate = LocalDate.now();
        List<CategoryReport> list = categoryReportService.countCategoryReport(localDate);
        return list;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public Result autoRespawnCategoryReport(){
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);
        String inserted = categoryReportService.autoRespawnCategoryReport(localDate);
        System.out.println(inserted);
        Result result = new Result();
        System.out.println(result);
        return result;
    }
    @Scheduled(cron = "* * * * * ?")
    public void autoSpawnCountTrade(){
        String date = "2019-03-14";
        System.out.println(new Date());
        countTradeService.autoSpawnCountTrade(date);
    }

}
