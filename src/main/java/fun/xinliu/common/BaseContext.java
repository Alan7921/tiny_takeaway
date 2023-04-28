package fun.xinliu.common;

/**
 * create by: Xin Liu
 * description: 基于ThreadLocal的封装工具类，用于保存和获取当前登陆用户的id
 * create time: 2023/4/14 4:00 PM
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
