package cn.reve.dao;

import cn.reve.pojo.goods.ExamineLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import tk.mybatis.mapper.common.Mapper;

public interface ExamineLogMapper extends Mapper<ExamineLog>{

    @Insert("insert into tb_examine_log values (#{id}, #{createTime},#{updateTime},#{spuId})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertExamineLogMapper(ExamineLog examineLog);
}
