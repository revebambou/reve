package cn.reve.utils;

import cn.reve.entity.PageResult;
import com.github.pagehelper.Page;

public class PageUtils {

    public static<T> PageResult<T> setPageResult(Page<T> page){
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setRows(page.getResult());
        pageResult.setTotal(page.getTotal());
        return pageResult;
    }
}
