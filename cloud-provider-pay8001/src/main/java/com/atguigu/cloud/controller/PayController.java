package com.atguigu.cloud.controller;

import com.atguigu.cloud.entities.Pay;
import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.resp.ReturnCodeNum;
import com.atguigu.cloud.service.PayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@Tag(name = "支付模块",description = "支付crud")
public class PayController {

    @Resource
    private PayService payService;

    @Operation(summary = "新增",description = "新增支付流水方法,json串做参数")
    @PostMapping(value = "/pay/add")
    public ResultData<String> addPay(@RequestBody Pay pay) {
        log.info(pay.toString());
        return ResultData.success("成功插入记录，返回值为："+payService.add(pay));
    }

    @Operation(summary = "删除",description = "删除支付流水方法,json串做参数")
    @DeleteMapping(value = "/pay/delete/{id}")
    public ResultData<Integer> deletePay(@PathVariable("id") Integer id){

        return ResultData.success(payService.delete(id));
    }

    @PostMapping(value = "/pay/update")
    public ResultData<String> updatePay(@RequestBody PayDTO payDTO) {
        Pay pay = new Pay();
        //拷贝 把payDTO的数据拷贝到pay
        BeanUtils.copyProperties(payDTO,pay);
        return ResultData.success("成功修改记录，返回值为：" + payService.update(pay));
    }

    @GetMapping(value = "/pay/getById/{id}")
    public ResultData<Pay> getPayById(@PathVariable("id") Integer id){
        if(id==-1) {
            throw new RuntimeException("id不能为负数");
        }
        //故意暂停
        try {
            TimeUnit.SECONDS.sleep(62);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ResultData.success(payService.getById(id));
    }

    @GetMapping(value = "/pay/getAll")
    public ResultData<List<Pay>> getAll() {
        return ResultData.success(payService.getAll());
    }

    @GetMapping(value = "/pay/error")
    public ResultData<Integer> getPayError() {
        Integer integer = Integer.valueOf(200);
        try{
            int age = 10/0;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.fail(ReturnCodeNum.RC500.getCode(), e.getMessage());
        }
        return ResultData.success(integer);
    }

    @Value("${server.port}")
    private String port;

    @GetMapping(value = "/pay/get/info")
    private String getInfoByConsul(@Value("${atguigu.info}") String atguiguInfo) {
        return "信息是："+atguiguInfo+"\t port:"+port;
    }

}
