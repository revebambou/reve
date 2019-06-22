package cn.reve.service.goods;

import cn.reve.entity.ExamineLogs;

public interface ExamineLogService {

    ExamineLogs findLogBySpuId(String spuId);

}
