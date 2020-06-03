package com.lucius.secondkill.rabbitmq;


import com.lucius.secondkill.entity.SkGoods;
import com.lucius.secondkill.entity.SkOrder;
import com.lucius.secondkill.entity.SkUser;
import com.lucius.secondkill.redis.RedisService;
import com.lucius.secondkill.service.SecKillService;
import com.lucius.secondkill.service.SkGoodsService;
import com.lucius.secondkill.service.SkOrderService;
import com.lucius.secondkill.service.SkUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//接收者
@Service
public class MQReceiver {
	@Autowired
	SkGoodsService skGoodsService;
	@Autowired
	RedisService redisService;
	@Autowired
	SkUserService skUserService;
	//作为秒杀功能事务的Service
	@Autowired
	SecKillService secKillService;
	@Autowired
	SkOrderService skOrderService;
	
	private static Logger log= LoggerFactory.getLogger(MQReceiver.class);
	
	//秒杀的业务实际上都是在这里完成的
	@RabbitListener(queues=MQConfig.SECKILL_QUEUE)//指明监听的是哪一个queue
	public void receiveSeckillMessage(String message) {
		log.info("receiveMiaosha message:"+message);
		//通过string类型的message还原成bean
		//拿到了秒杀信息之后。开始业务逻辑秒杀，
		SeckillMessage mm=RedisService.stringToBean(message, SeckillMessage.class);
		SkUser user=mm.getUser();
		long goodsId=mm.getGoodsId();
		SkGoods skGoods= skGoodsService.getGoodsByGoodsId(goodsId);
		//注意此时查的是数据库
		int  stockcount=skGoods.getStockCount();
		//1.判断库存不足
		if(stockcount<=0) {//失败
			//model.addAttribute("errorMessage", CodeMsg.MIAOSHA_OVER_ERROR);
			return;
		}
		//2.判断这个秒杀订单形成没有，判断是否已经秒杀到了，避免一个账户秒杀多个商品
		SkOrder order = skOrderService.queryOrderByUserIdAndGoodsId(user.getId(), goodsId);
		if (order != null) {// 重复下单
			// model.addAttribute("errorMessage", CodeMsg.REPEATE_MIAOSHA);
			return;
		}
		//原子操作：1.库存减1，2.下订单，3.写入秒杀订单--->是一个事务
		//miaoshaService.miaosha(user,goodsvo);
		secKillService.seckill(user,skGoods);
		
	}
	
	
	
	
	
//	@RabbitListener(queues=MQConfig.QUEUE)//指明监听的是哪一个queue
//	public void receive(String message) {
//		log.info("receive message:"+message);
//	}
//	
//	@RabbitListener(queues=MQConfig.TOPIC_QUEUE1)//指明监听的是哪一个queue
//	public void receiveTopic1(String message) {
//		log.info("receiveTopic1 message:"+message);
//	}
//	
//	@RabbitListener(queues=MQConfig.TOPIC_QUEUE2)//指明监听的是哪一个queue
//	public void receiveTopic2(String message) {
//		log.info("receiveTopic2 message:"+message);
//	}
//	
//	@RabbitListener(queues=MQConfig.HEADER_QUEUE)//指明监听的是哪一个queue
//	public void receiveHeaderQueue(byte[] message) {
//		log.info("receive Header Queue message:"+new String(message));
//	}
}
