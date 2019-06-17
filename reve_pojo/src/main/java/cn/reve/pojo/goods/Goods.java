package cn.reve.pojo.goods;

import java.io.Serializable;
import java.util.List;

public class Goods implements Serializable {

    private Sku sku;

    private List<Spu> spus;

    public Goods(Sku sku, List<Spu> spus) {
        this.sku = sku;
        this.spus = spus;
    }

    public Goods() {
    }

    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }

    public List<Spu> getSpus() {
        return spus;
    }

    public void setSpus(List<Spu> spus) {
        this.spus = spus;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "sku=" + sku +
                ", spus=" + spus +
                '}';
    }
}
