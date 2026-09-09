package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ShoppingCartService {

    /**
     *添加购物车
     * @param shoppingCartDTO
     * @return
     */
    void add(@RequestBody ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车列表
     * @return
     */
    List<ShoppingCart> showShoppingCart();

    /**
     * 删除购物车中一个商品
     * @param shoppingCartDTO
     */
    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 清空购物车
     */
    void cleanShoppingCart();
}