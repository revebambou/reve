package cn.reve.service.order;

public interface CountTradeService {

    //Using OrderTask to invoke this method automatically everyday
    boolean autoSpawnCountTrade(String date);
}
