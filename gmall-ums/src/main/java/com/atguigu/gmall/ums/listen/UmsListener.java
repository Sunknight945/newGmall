package com.atguigu.gmall.ums.listen;

import com.atguigu.gmall.ums.dao.MemberDao;
import com.atguigu.gmall.ums.ov.UserBoundsVo;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Component
public class UmsListener {

	private final MemberDao memberDao;

	public UmsListener(MemberDao memberDao) {
		this.memberDao = memberDao;
	}

	@RabbitListener(bindings = @QueueBinding(
		value = @Queue(value = "USER-BOUNDS-QUEUE", durable = "true"),
		exchange = @Exchange(value = "GMALL-ORDER-EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
		key = {"user.bounds"}
	))
	public void updateBounds(UserBoundsVo boundsVo) {
		this.memberDao.updateBoundsById(boundsVo.getMemberId(),boundsVo.getGrowth(),boundsVo.getIntegration());
	}

}


