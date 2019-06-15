package cn.reve.utils;

import cn.reve.entity.PageResult;
import com.github.pagehelper.Page;
import tk.mybatis.mapper.entity.Example;

public class MapperUtils {

    public static Example andEqualToWithSingleValue(Class cls, String baseName, int value){
        Example example = new Example(cls);
        if(value!=0){
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo(baseName, value);
        }
        return example;
    }

    public static Example andEqualToWithSingleValue(Class cls, String baseName, String value){
        Example example = new Example(cls);
        if(value!=null && value.length()!=0){
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo(baseName, value);
        }
        return example;
    }

}
