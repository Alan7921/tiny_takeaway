package fun.xinliu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.xinliu.common.Result;
import fun.xinliu.dto.DishDto;
import fun.xinliu.entity.Category;
import fun.xinliu.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result<String> save(@RequestBody Category category) {
        log.info("category: {}", category);
        categoryService.save(category);
        return Result.success("添加成功");
    }

    @GetMapping("/page")
    public Result<Page> page(Integer page, Integer pageSize) {
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize);

        // 声明Page对象
        Page pageInfo = new Page(page, pageSize);

        // 构造条件构造器
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<Category>();

        // 设置返回结果的排序
        lqw.orderByDesc(Category::getSort);

        // 执行查询
        categoryService.page(pageInfo, lqw);

        return Result.success(pageInfo);
    }

    @GetMapping("/list")
    public Result<List<Category>> getCategoryList(Category category) {

        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper();

        lqw.eq(category.getType() != null, Category::getType, category.getType());

        lqw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(lqw);

        return Result.success(list);
    }

    @DeleteMapping
    public Result<String> delete(Long id) {
        categoryService.removeCategory(id);
        return Result.success("成功删除指定分类!");
    }

    @PutMapping
    public Result<String> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return Result.success("成功修改指定分类!");
    }


}
