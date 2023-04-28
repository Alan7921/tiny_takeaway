package fun.xinliu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.xinliu.entity.Category;
import fun.xinliu.entity.Employee;

public interface CategoryService extends IService<Category> {
    public void removeCategory(Long id);
}
