package cn.reve.controller.goods;

import cn.reve.pojo.goods.Goods;
import com.alibaba.dubbo.config.annotation.Reference;
import cn.reve.entity.PageResult;
import cn.reve.entity.Result;
import cn.reve.pojo.goods.Spu;
import cn.reve.service.goods.SpuService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/spu")
public class SpuController {

    @Reference
    private SpuService spuService;

    @PostMapping("/saveGoods")
    public Result saveGoods(@RequestBody Goods goods){
        System.out.println(goods);
        spuService.saveGoods(goods);
        return new Result();
    }

    @PostMapping("/updateGoods")
    public Result updateGoods(@RequestBody Goods goods){
        System.out.println(goods);
        spuService.updateGoods(goods);
        return new Result();
    }

    @GetMapping("/findGoodsBySpuId")
    public Goods findGoodsBySpuId(String spuId){
        Goods goods = spuService.findGoodsBySpuId(spuId);
        System.out.println(goods);
        return goods;
    }

    @GetMapping("/findAll")
    public List<Spu> findAll(){
        return spuService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult<Spu> findPage(int page, int size){
        return spuService.findPage(page, size);
    }

    @PostMapping("/findList")
    public List<Spu> findList(@RequestBody Map<String,Object> searchMap){
        return spuService.findList(searchMap);
    }

    @PostMapping("/findPage")
    public PageResult<Spu> findPage(@RequestBody Map<String,Object> searchMap,int page, int size){
        return  spuService.findPage(searchMap,page,size);
    }

    @GetMapping("/findById")
    public Spu findById(String id){
        return spuService.findById(id);
    }


    @PostMapping("/add")
    public Result add(@RequestBody Spu spu){
        spuService.add(spu);
        return new Result();
    }

    @PostMapping("/update")
    public Result update(@RequestBody Spu spu){
        spuService.update(spu);
        return new Result();
    }

    @GetMapping("/delete")
    public Result delete(String id){
        spuService.delete(id);
        return new Result();
    }

    @GetMapping("/examine")
    public Result examine(String spuId, String radio, String memo){
        spuService.updateSpuViaExamine(spuId, radio, memo);
        return new Result();
    }

    @PostMapping("/batchExamineByIds")
    public Result batchExamineByIds(@RequestBody String[] spuIds, String radio, String memo){
        System.out.println(spuIds.toString()+"==="+radio+"==="+memo);
        spuService.batchExamineByIds(spuIds, radio, memo);
        return new Result();
    }

}
