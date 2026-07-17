package com.mukesh.order.entity.security;

import com.mukesh.order.entity.security.PermissionName;
import com.mukesh.order.entity.security.RoleName;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public final class RolePermissionMapping {

    private RolePermissionMapping() {
    }

    private static final Map<RoleName, Set<PermissionName>> ROLE_PERMISSIONS =
            new EnumMap<>(RoleName.class);

    static {

        ROLE_PERMISSIONS.put(
                RoleName.ROLE_ADMIN,
                EnumSet.allOf(PermissionName.class)
        );

        ROLE_PERMISSIONS.put(
                RoleName.ROLE_MANAGER,
                EnumSet.of(
                        PermissionName.ORDER_CREATE,
                        PermissionName.ORDER_READ,
                        PermissionName.ORDER_UPDATE,
                        PermissionName.ORDER_CANCEL,
                        PermissionName.PAYMENT_VIEW,
                        PermissionName.CUSTOMER_VIEW,
                        PermissionName.AUDIT_READ
                )
        );

        ROLE_PERMISSIONS.put(
                RoleName.ROLE_WAREHOUSE,
                EnumSet.of(
                        PermissionName.ORDER_READ,
                        PermissionName.INVENTORY_VIEW,
                        PermissionName.INVENTORY_RESERVE,
                        PermissionName.INVENTORY_RELEASE,
                        PermissionName.INVENTORY_UPDATE
                )
        );

        ROLE_PERMISSIONS.put(
                RoleName.ROLE_FINANCE,
                EnumSet.of(
                        PermissionName.PAYMENT_CREATE,
                        PermissionName.PAYMENT_VIEW,
                        PermissionName.PAYMENT_REFUND,
                        PermissionName.PAYMENT_CANCEL,
                        PermissionName.AUDIT_READ
                )
        );

        ROLE_PERMISSIONS.put(
                RoleName.ROLE_SUPPORT,
                EnumSet.of(
                        PermissionName.ORDER_READ,
                        PermissionName.CUSTOMER_VIEW,
                        PermissionName.PAYMENT_VIEW
                )
        );

        ROLE_PERMISSIONS.put(
                RoleName.ROLE_CUSTOMER,
                EnumSet.of(
                        PermissionName.ORDER_CREATE,
                        PermissionName.ORDER_READ,
                        PermissionName.ORDER_CANCEL
                )
        );
    }

    public static Set<PermissionName> getPermissions(RoleName roleName) {
        return ROLE_PERMISSIONS.getOrDefault(roleName, EnumSet.noneOf(PermissionName.class));
    }

}