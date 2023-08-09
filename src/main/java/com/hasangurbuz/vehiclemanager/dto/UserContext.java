package com.hasangurbuz.vehiclemanager.dto;

import com.hasangurbuz.vehiclemanager.domain.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserContext {
    private Long userId;
    private String name;
    private String surname;
    private Long companyId;
    private String companyName;
    private UserRole userRole;

    private static final ThreadLocal<UserContext> threadLocal = new InheritableThreadLocal<>();

    public static UserContext create() {
        UserContext context = new UserContext();
        threadLocal.set(context);

        return context;
    }

    public static UserContext get() {
        return threadLocal.get();
    }

    public static void unset() {
        threadLocal.remove();
    }

}
