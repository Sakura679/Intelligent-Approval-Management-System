package com.iams.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/5 12:39
 * @Desc:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserContext {

    private Long userId;
    private String account;
    private String token;

    private static final ThreadLocal<UserContext> threadLocal = new ThreadLocal<>();

    public static void setCurrentUser(UserContext user) {
        threadLocal.set(user);
    }

    public static UserContext getCurrentUser() {
        return threadLocal.get();
    }

    public static Long getCurrentUserId() {
        UserContext user = threadLocal.get();
        return user != null ? user.getUserId() : null;
    }

    public static String getCurrentAccount() {
        UserContext user = threadLocal.get();
        return user != null ? user.getAccount() : null;
    }

    public static String getCurrentToken() {
        UserContext user = threadLocal.get();
        return user != null ? user.getToken() : null;
    }

    public static void clear() {
        threadLocal.remove();
    }
}
