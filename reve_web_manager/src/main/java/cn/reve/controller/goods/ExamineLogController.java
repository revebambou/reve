package cn.reve.controller.goods;

import cn.reve.entity.ExamineLogs;
import cn.reve.pojo.goods.ExamineLog;
import cn.reve.pojo.goods.ExamineLogRecord;
import cn.reve.service.goods.ExamineLogService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/examineLog")
public class ExamineLogController {

    @Reference
    private ExamineLogService examineLogService;

    public ExamineLogs findExamineLogBySpuId(String spuId){
        ExamineLogs examineLogs = examineLogService.findLogBySpuId(spuId);
        System.out.println(examineLogs);
        return examineLogs;
    }
}
