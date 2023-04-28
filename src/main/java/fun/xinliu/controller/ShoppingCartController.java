package fun.xinliu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import fun.xinliu.common.BaseContext;
import fun.xinliu.common.Result;
import fun.xinliu.entity.ShoppingCart;
import fun.xinliu.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public Result<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info("购物车数据: {}", shoppingCart);

        // 设置userId
        shoppingCart.setUserId(BaseContext.getCurrentId());

        // 查询当前菜品或者套餐是否在购物车中

        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper();
        lqw.eq(ShoppingCart::getUserId, shoppingCart.getUserId());
        lqw.eq(ShoppingCart::getName, shoppingCart.getName());

        ShoppingCart cartItem = shoppingCartService.getOne(lqw);

        // 如果查到，则修改number字段即可
        if (cartItem != null) {
            cartItem.setNumber(cartItem.getNumber() + 1);
            shoppingCartService.updateById(cartItem);
        } else {
            // 如果查不到，则添加到购物车
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartItem = shoppingCart;
        }

        return Result.success(cartItem);
    }


    @GetMapping("/list")
    public Result<List<ShoppingCart>> list() {

        log.info("查看购物车...");
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper();
        lqw.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        lqw.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> carts = shoppingCartService.list(lqw);

        return Result.success(carts);
    }

    @DeleteMapping("/clean")
    public Result<String> clean() {

        log.info("清空购物车...");
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper();
        lqw.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(lqw);

        return Result.success("成功清空购物车...");
    }


    @PostMapping("/sub")
    public Result<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {

        log.info("减少购物车项的数量...");
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper();
        lqw.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        if(shoppingCart.getDishId() != null){
            lqw.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        }else{
            lqw.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        ShoppingCart cart = shoppingCartService.getOne(lqw);
        cart.setNumber(cart.getNumber() - 1);

        int cartNum = cart.getNumber();

        if(cartNum == 0) {
            shoppingCartService.removeById(cart.getId());
        }else{
            shoppingCartService.updateById(cart);
        }

        return Result.success(cart);
    }


}
