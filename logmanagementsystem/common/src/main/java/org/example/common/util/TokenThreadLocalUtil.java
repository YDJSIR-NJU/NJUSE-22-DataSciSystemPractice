package org.example.common.util;


import org.example.common.vo.UserVo;

public class TokenThreadLocalUtil {

    private static final ThreadLocal<UserVo> threadLocal = new ThreadLocal<>();


    public static UserVo get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }

    public static void set(UserVo user) {
        threadLocal.set(user);
    }

}
