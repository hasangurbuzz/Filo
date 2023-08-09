package com.hasangurbuz.vehiclemanager.api;

import com.hasangurbuz.vehiclemanager.domain.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiContext {
    private Long userId;
    private String name;
    private String surname;
    private Long companyId;
    private String companyName;
    private UserRole userRole;

    private static final ThreadLocal<ApiContext> threadLocal = new InheritableThreadLocal<>();

    public static ApiContext create() {
        ApiContext context = new ApiContext();
        threadLocal.set(context);

        return context;
    }

    public static ApiContext get() {
        return threadLocal.get();
    }

    public static void unset() {
        threadLocal.remove();
    }

}
