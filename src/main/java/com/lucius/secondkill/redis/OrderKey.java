package com.lucius.secondkill.redis;

/**

 */
public class OrderKey extends BasePrefix {

    public OrderKey(String prefix) {
        super(prefix);
    }

    private OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static OrderKey SeckillOrderByUidGid = new OrderKey(60,"SeckillOrder");
    public static OrderKey SeckillOrderByUidGidHtml = new OrderKey(60,"SeckillOrderHtml");
}
