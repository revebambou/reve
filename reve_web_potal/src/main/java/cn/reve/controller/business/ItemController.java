package cn.reve.controller.business;

import cn.reve.pojo.goods.Goods;
import cn.reve.pojo.goods.Sku;
import cn.reve.pojo.goods.Spu;
import cn.reve.service.goods.SpuService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;

import java.util.List;

@RestController
public class ItemController {

    @Reference
    private SpuService spuService;

    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping("/generalHTML")
    public void generalHTML(String spuId){
        Goods goods = spuService.findGoodsBySpuId(spuId);
        Spu spu = goods.getSpu();
        List<Sku> skuList = goods.getSkuList();

    }

}
