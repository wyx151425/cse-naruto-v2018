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
        public static final String ROLE_USER = "ROLE_USER";
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
        public static final String ROLE_TECH_EMPLOYEE = "ROLE_TECH_EMPLOYEE";
        public static final String ROLE_QA_ENV_EMPLOYEE = "ROLE_QA_ENV_EMPLOYEE";
        public static final String ROLE_PURCHASE_EMPLOYEE = "ROLE_PURCHASE_EMPLOYEE";
        public static final String ROLE_ASSEMBLY_EMPLOYEE = "ROLE_ASSEMBLY_EMPLOYEE";
        public static final String ROLE_PRO_OPE_EMPLOYEE = "ROLE_PRO_OPE_EMPLOYEE";
    }

    public static final class Premissions {
        public static final String MATERIAL_STANDARD = "MATERIAL_STANDARD";
        public static final String MATERIAL_EDIT_TECH_PART = "MATERIAL_EDIT_TECH_PART";
        public static final String MATERIAL_EDIT_QA_ENV_PART = "MATERIAL_EDIT_QA_ENV_PART";
        public static final String MATERIAL_EDIT_PURCHASE_PART = "MATERIAL_EDIT_PURCHASE_PART";
        public static final String MATERIAL_EDIT_ASSEMBLY_PART = "MATERIAL_EDIT_ASSEMBLY_PART";
        public static final String MATERIAL_EDIT_PRO_OPE_PART = "MATERIAL_EDIT_PRO_OPE_PART";
    }

    public static final class MaterialProperty {
        public static final String STATUS = "status";
        public static final String SOURCE_MARK = "sourceMark";
        public static final String TECHNOLOGY_DEPT_MARK = "technologyDeptMark";
        public static final String QUALIFIED_DEPT_MARK = "qualifiedDeptMark";
        public static final String PURCHASE_DEPT_MARK = "purchaseDeptMark";
        public static final String ASSEMBLY_DEPT_MARK = "assemblyDeptMark";
        public static final String OPERATE_DEPT_MARK = "operateDeptMark";
    }

    public static final class PermissionStatus {
        public static final int COMMON = 1;
    }

    public static final class MaterialStatus {
        public static final int IMPERFECT = 1;
        public static final int CAN_EXPORT = 2;
        public static final int EXPORTED = 3;
    }
}
