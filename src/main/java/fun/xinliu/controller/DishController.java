package fun.xinliu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.xinliu.common.CustomException;
import fun.xinliu.common.Result;
import fun.xinliu.dto.DishDto;
import fun.xinliu.entity.Category;
import fun.xinliu.entity.Dish;
import fun.xinliu.entity.DishFlavor;
import fun.xinliu.service.CategoryService;
import fun.xinliu.service.DishFlavorService;
import fun.xinliu.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    DishService dishService;
    @Autowired
    DishFlavorService dishFlavorService;

    @Autowired
    CategoryService categoryService;


    @PostMapping()
    public Result<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.saveDishWithFlavor(dishDto);
        return Result.success("添加成功");
    }

    @GetMapping("/page")
    public Result<Page> page(Integer page, Integer pageSize, String name) {


        // 首先构造分页构造器，因为Dish对象不包含categoryName属性，所以我们要引入一个Dto类
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        // 设置过滤条件
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.like(name != null, Dish::getName, name);

        // 设置排序方法
        lqw.orderByDesc(Dish::getUpdateTime);

        // 查询dish数据
        dishService.page(pageInfo, lqw);

        // 将查到的dish数据拷贝到dto中
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        // 处理records字段
        List<Dish> records = pageInfo.getRecords();

        // 用dish的pageInfo中的records中的每个item的categoryId查询到对应的categoryName赋值给dishDto对象
        List<DishDto> list =  records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            return dishDto;
        }).collect(Collectors.toList());


        dishDtoPage.setRecords(list);

        return Result.success(dishDtoPage);
    }

    @PutMapping()
    public Result<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());


        dishService.updateWithFlavor(dishDto);

        return Result.success("添加成功");
    }

    @GetMapping("/{id}")
    public Result<DishDto> getById(@PathVariable("id") Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return Result.success(dishDto);
    }


    @PostMapping("/status/{status}")
    public Result<String> changeStatus(@PathVariable("status") int status, @RequestParam List<Long> ids) {

        log.info("operation is: {}",status);
        log.info(ids.toString());

        LambdaUpdateWrapper<Dish> luw = new LambdaUpdateWrapper();
        luw.in(ids != null, Dish::getId, ids);
        luw.set(Dish::getStatus, status);

        dishService.update(luw);

        return Result.success("菜品状态已经更改成功！");
    }

    @DeleteMapping
    public Result<String> deleteDish(@RequestParam List<Long> ids){

        dishService.deleteWithFlavor(ids);

        return Result.success("删除成功");
    }


//    @GetMapping("/list")
//    public Result<List<Dish>> getDishesByCategoryId(@RequestParam Long categoryId) {
//
//        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper();
//        lqw.eq(categoryId != null, Dish::getCategoryId, categoryId);
//        List<Dish> dishes = dishService.list(lqw);
//
//        return Result.success(dishes);
//    }

//    @GetMapping("/list")
//    public Result<List<Dish>> getDishesByCategoryId(Dish dish) {
//
//        // 构造查询条件
//        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper();
//        lqw.eq(dish != null, Dish::getCategoryId, dish.getCategoryId());
//
//        // 注意只查询那些处于在售状态的菜品
//        lqw.eq(Dish::getStatus, 1);
//
//        // 构造排序条件
//        lqw.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//
//        // 查询
//        List<Dish> dishes = dishService.list(lqw);
//
//        return Result.success(dishes);
//    }

    @GetMapping("/list")
    public Result<List<DishDto>> getDishesByCategoryId(Dish dish) {

        // 构造查询条件
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper();
        lqw.eq(dish != null, Dish::getCategoryId, dish.getCategoryId());

        // 注意只查询那些处于在售状态的菜品
        lqw.eq(Dish::getStatus, 1);

        // 构造排序条件
        lqw.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        // 查询
        List<Dish> dishes = dishService.list(lqw);

        List<DishDto> dishDtos = dishes.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);

            if(dishFlavors != null) {
                dishDto.setFlavors(dishFlavors);
            }

            return dishDto;
        }).collect(Collectors.toList());
        return Result.success(dishDtos);
    }
}
