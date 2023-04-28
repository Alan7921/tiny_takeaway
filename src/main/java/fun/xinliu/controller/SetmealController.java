package fun.xinliu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.xinliu.common.CustomException;
import fun.xinliu.common.Result;
import fun.xinliu.dto.SetmealDto;
import fun.xinliu.entity.*;
import fun.xinliu.service.CategoryService;
import fun.xinliu.service.SetmealDishService;
import fun.xinliu.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result<String> save(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.saveSetmealWithDish(setmealDto);
        return Result.success("新增套餐成功");
    }

    @GetMapping("/page")
    public Result<Page<SetmealDto>> page(int page, int pageSize, String name) {

        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> pageDto = new Page<>(page, pageSize);


        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper();
        lqw.like(name != null, Setmeal::getName, name);
        lqw.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, lqw);

        BeanUtils.copyProperties(pageInfo, pageDto, "records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item)->{

            // 声明setmealDto对象，拷贝setMeal中的属性
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);

            // 用setMeal中的categoryId查询对应的name，然后赋值给dto
            Long categoryId = item.getCategoryId();

            Category category = categoryService.getById(categoryId);

            if(category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }

            return setmealDto;
        }).collect(Collectors.toList());

        pageDto.setRecords(list);

        return Result.success(pageDto);
    }

    @DeleteMapping
    public Result<String> deleteSetmeal(@RequestParam List<Long> ids){
        setmealService.removeWithDish(ids);
        return Result.success("删除套餐成功");
    }

    @PostMapping("/status/{status}")
    public Result<String> changeStatus(@PathVariable("status") int status, @RequestParam List<Long> ids) {

        LambdaUpdateWrapper<Setmeal> luw = new LambdaUpdateWrapper();
        luw.in(ids != null, Setmeal::getId, ids);
        luw.set(Setmeal::getStatus, status);

        setmealService.update(luw);

        return Result.success("套餐状态已经更改成功！");
    }


    @GetMapping("/list")
    public Result<List<Setmeal>> getCategoryList(Long categoryId, int status) {

        log.info("category id is : {}", categoryId);
        log.info("status is {}", status);

        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.eq(categoryId != null, Setmeal::getCategoryId, categoryId);
        lqw.eq(Setmeal::getStatus, status);

        List<Setmeal> list = setmealService.list(lqw);

        return Result.success(list);
    }


}
