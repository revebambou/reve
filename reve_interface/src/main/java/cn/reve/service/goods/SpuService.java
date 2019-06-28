package cn.reve.service.goods;
import cn.reve.entity.PageResult;
import cn.reve.pojo.goods.Goods;
import cn.reve.pojo.goods.Spu;

import java.util.*;

/**
 * spu业务逻辑层
 */
public interface SpuService {


    public List<Spu> findAll();


    public PageResult<Spu> findPage(int page, int size);


    public List<Spu> findList(Map<String,Object> searchMap);


    public PageResult<Spu> findPage(Map<String,Object> searchMap,int page, int size);


    public Spu findById(String id);

    public void add(Spu spu);


    public void update(Spu spu);


    public void delete(String id);

    void saveGoods(Goods goods);

    Goods findGoodsBySpuId(String spuId);

    void updateGoods(Goods goods);

    void updateSpuViaExamine(String spuId, String radio, String memo);

    void batchExamineByIds(String[] spuIds, String radio, String memo);

}
