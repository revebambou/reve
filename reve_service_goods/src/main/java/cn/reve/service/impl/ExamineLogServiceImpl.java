package cn.reve.service.impl;

import cn.reve.dao.ExamineLogMapper;
import cn.reve.dao.ExamineLogRecordMapper;
import cn.reve.entity.ExamineLogs;
import cn.reve.pojo.goods.ExamineLog;
import cn.reve.pojo.goods.ExamineLogRecord;
import cn.reve.service.goods.ExamineLogService;
import cn.reve.utils.MapperUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExamineLogServiceImpl implements ExamineLogService {

    @Autowired
    private ExamineLogRecordMapper examineLogRecordMapper;

    @Autowired
    private ExamineLogMapper examineLogMapper;

    @Override
    public ExamineLogs findLogBySpuId(String spuId) {
        Example example = MapperUtils.andEqualToWithSingleValue(ExamineLog.class, "spuId", spuId);
        ExamineLog examineLog = (ExamineLog) examineLogMapper.selectByExample(example);
        int examineId = examineLog.getId();

        Example example1 = MapperUtils.andEqualToWithSingleValue(ExamineLogRecord.class, "examineId", examineId);
        List<ExamineLogRecord> examineLogRecordList = examineLogRecordMapper.selectByExample(example1);

        ExamineLogs examineLogs = new ExamineLogs(examineLog, examineLogRecordList);
        return examineLogs;
    }
}
