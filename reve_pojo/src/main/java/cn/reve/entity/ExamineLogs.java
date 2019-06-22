package cn.reve.entity;

import cn.reve.pojo.goods.ExamineLog;
import cn.reve.pojo.goods.ExamineLogRecord;

import java.io.Serializable;
import java.util.List;

public class ExamineLogs implements Serializable {
    private ExamineLog examineLog;
    private List<ExamineLogRecord> examineLogRecordList;

    public ExamineLogs(ExamineLog examineLog, List<ExamineLogRecord> examineLogRecordList) {
        this.examineLog = examineLog;
        this.examineLogRecordList = examineLogRecordList;
    }

    public ExamineLogs() {
    }

    public ExamineLog getExamineLog() {
        return examineLog;
    }

    public void setExamineLog(ExamineLog examineLog) {
        this.examineLog = examineLog;
    }

    public List<ExamineLogRecord> getExamineLogRecordList() {
        return examineLogRecordList;
    }

    public void setExamineLogRecordList(List<ExamineLogRecord> examineLogRecordList) {
        this.examineLogRecordList = examineLogRecordList;
    }

    @Override
    public String toString() {
        return "ExamineLogs{" +
                "examineLog=" + examineLog +
                ", examineLogRecordList=" + examineLogRecordList +
                '}';
    }
}
