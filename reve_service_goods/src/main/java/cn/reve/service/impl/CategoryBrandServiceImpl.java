package cn.reve.service.impl;

import cn.reve.service.goods.CategoryBrandService;
import com.alibaba.dubbo.config.annotation.Service;

@Service
public class CategoryBrandServiceImpl implements CategoryBrandService {
    @Override
    public int findBrandIdByCategoryId(int categoryId) {
        return 0;
    }
}
