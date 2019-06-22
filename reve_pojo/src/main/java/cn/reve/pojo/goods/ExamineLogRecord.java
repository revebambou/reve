package cn.reve.pojo.goods;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "tb_examine_log_record")
public class ExamineLogRecord implements Serializable {
    @Id
    private Integer id;
    @Id
    private Integer examineId;
    private String status;
    private String memo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExamineId() {
        return examineId;
    }

    public void setExamineId(Integer examineId) {
        this.examineId = examineId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return "ExamineLogRecord{" +
                "id=" + id +
                ", examineId=" + examineId +
                ", status='" + status + '\'' +
                ", memo='" + memo + '\'' +
                '}';
    }
}
