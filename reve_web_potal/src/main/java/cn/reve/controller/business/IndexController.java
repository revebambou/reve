package cn.reve.controller.business;

import cn.reve.pojo.business.Ad;
import cn.reve.service.business.AdService;
import cn.reve.service.goods.CategoryService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Reference
    private AdService adService;

    @Reference
    private CategoryService categoryService;

    @GetMapping("/index")
    public String index(Model model){
        List<Ad> lbt = adService.findByPosition("web_index_lb");
        System.out.println(lbt);
        model.addAttribute("lbt",lbt);
        List<Map> categoryList = categoryService.findCategoriesWithShowingInForeground();
        model.addAttribute("categoryList", categoryList);
        return "index";
    }
}
