package cn.reve.controller.business;

import cn.reve.pojo.business.Ad;
import cn.reve.service.business.AdService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ad")
public class AdController {

    @Reference
    private AdService adService;

    @GetMapping("/generalHTML")
    public String generalHTML(Model model){
        //Carousel
        List<Ad> indexCarouselList = adService.findByPosition("index_lb");
        model.addAttribute("indexCarouselList", indexCarouselList);
        return "index";
    }
}
