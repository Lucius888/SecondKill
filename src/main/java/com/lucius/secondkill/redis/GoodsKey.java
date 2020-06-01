package com.lucius.secondkill.redis;

public class GoodsKey extends BasePrefix {

    private GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodsKey GoodsList = new GoodsKey(60, "GoodsList");
    public static GoodsKey GoodsDetail = new GoodsKey(60, "GoodsDetail");
    public static GoodsKey GoodsStock = new GoodsKey(60, "GoodsStock");
    public static GoodsKey GoodsListHtml = new GoodsKey(60, "GoodsListHtml");
    public static GoodsKey GoodsDetailHtml = new GoodsKey(60, "GoodsDetailHtml");
    public static GoodsKey GoodsStockHtml = new GoodsKey(60, "GoodsStockHtml");
}
