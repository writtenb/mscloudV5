package com.atguigu.cloud.service.impl;

import com.atguigu.cloud.apis.AccountFeignApi;
import com.atguigu.cloud.apis.StorageFeignApi;
import com.atguigu.cloud.entities.Order;
import com.atguigu.cloud.mapper.OrderMapper;
import com.atguigu.cloud.service.OrderService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private StorageFeignApi storageFeignApi;

    @Resource
    private AccountFeignApi accountFeignApi;

    @Override
    @GlobalTransactional(name = "llpp-create-order",rollbackFor = Exception.class)
    public void create(Order order) {
        String xid = RootContext.getXID();
        log.info("------------开始创建订单:"+"\t"+"xid:"+xid);
        order.setStatus(0);
        int result = orderMapper.insertSelective(order);
        Order orderFromDB = null;
        if (result > 0) {
            orderFromDB = orderMapper.selectOne(order);
            log.info("------>新建订单成功,orderFromDB info:"+orderFromDB);
            System.out.println();
            //扣减库存
            log.info("------->开始扣减库存");
            storageFeignApi.decrease(orderFromDB.getProductId(),orderFromDB.getCount());
            log.info("扣减库存已完成");
            System.out.println();
            //扣减账户
            log.info("-------->开始扣减账户余额");
            accountFeignApi.decrease(orderFromDB.getUserId(),orderFromDB.getMoney());
            log.info("账户余额已经扣减完成");
            System.out.println();
            //修改订单状态，状态变为已完成
            orderFromDB.setStatus(1);
            //重新插入到数据库中去
            Example whereCondition = new Example(Order.class);
            Example.Criteria criteria = whereCondition.createCriteria();
            criteria.andEqualTo("userId",orderFromDB.getUserId());
            criteria.andEqualTo("status",0);

            int updateResult = orderMapper.updateByExampleSelective(orderFromDB, whereCondition);
            log.info("----->修改订单状态已完成:"+"\t"+updateResult);
            log.info("------>orderFromDB info "+orderFromDB);

        }
        System.out.println();
    }
}
