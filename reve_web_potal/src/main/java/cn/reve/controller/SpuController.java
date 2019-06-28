package cn.reve.controller;

import cn.reve.service.goods.SkuService;
import cn.reve.utils.InitValueUtil;
import cn.reve.utils.WebUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class SpuController {

    @Reference
    private SkuService skuService;

    @GetMapping("/search")
    public String search(@RequestParam Map<String, String> searchMap, Model model){
        System.out.println(searchMap);
/*        Map<String, String> mapNew = null;
        try {
            mapNew = WebUtil.convertCharsetToUTF8(searchMap);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        Map resultMap = skuService.searchMap(searchMap);

        StringBuffer sb = new StringBuffer("/search.do?");
        sb.append("&name="+searchMap.get("name"));
        String categoryName = searchMap.get("categoryName");
        if(categoryName!=null && !"".equals(categoryName)){
            sb.append("&categoryName="+categoryName);
        }
        String brandName = searchMap.get("brandName");
        if(brandName!=null && !"".equals(brandName)){
            sb.append("&brandName="+brandName);
        }
/*        String price = searchMap.get("price");
        if(price!=null && !"".equals(price)){
            sb.append("&price="+price);
        }*/

        searchMap = InitValueUtil.initKey("categoryName", searchMap);
        searchMap = InitValueUtil.initKey("brandName", searchMap);
        model.addAttribute("searchMap", searchMap);
        model.addAttribute("url", sb.toString());
//        System.out.println(initMap);
        System.out.println(resultMap);
        model.addAttribute("result", resultMap);
        return "search";//return a view
    }
}
