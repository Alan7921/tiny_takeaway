package fun.xinliu.dto;


import fun.xinliu.entity.Setmeal;
import fun.xinliu.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
