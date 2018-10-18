package com.cse.naruto.util;

/**
 * 常量类
 *
 * @author 王振琦
 * createAt 2018/09/19
 * updateAt 2018/09/19
 */
public class Constant {
    public static final String USER = "user";

    public static final class DocType {
        public static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        public static final String XLSX_UTF8 = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8";
    }

    public static final class UserStatus {
        public static final int COMMON = 1;
    }

    public static final class UserRoles {
        public static final String ROLE_USER = "ROLE_USER,";
        
        public static final String ROLE_ADMIN = "ROLE_ADMIN,";
    }

    public static final class PermissionStatus {
        public static final int COMMON = 1;
    }

    public static final class MaterialStatus {
        public static final int IMPERFECT = 1;
        public static final int PERFECTED_AND_CAN_EXPORT = 2;
        public static final int EXPORTED = 3;
    }
}
