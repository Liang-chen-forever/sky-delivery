package com.sky.controller.admin;


import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {


    public static final String key = "SHOP_STATUS";


    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 设置店铺营业状态
     * @param status 店铺营业状态
     */
    @PutMapping("/{status}")
    @ApiOperation(value = "修改店铺状态")
    public Result setStatus(@PathVariable Integer status){
        log.info("修改店铺状态为：{}", status == 1 ? "营业中" : "休息中");
        redisTemplate.opsForValue().set(key, status);
        return Result.success();
    }



    /**
     * 获取店铺营业状态
     */
    @GetMapping("/status")
    @ApiOperation(value = "获取店铺状态")
    public Result<Integer> getStatus(){
        Integer shopStatus = (Integer) redisTemplate.opsForValue().get(key);
        shopStatus = shopStatus == null ? 0 : shopStatus;
        log.info("获取店铺状态为：{}", shopStatus == 1 ? "营业中" : "休息中");
        return Result.success(shopStatus);
    }
}