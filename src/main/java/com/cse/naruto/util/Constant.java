package com.cse.naruto.util;

import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * 常量类
 *
 * @author 王振琦
 * createAt 2018/09/19
 * updateAt 2018/11/29
 */
public class Constant {
    public static final String USER = "user";

    public static final class DocType {
        public static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        public static final String XLSX_UTF8 = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8";
    }

    public static final class UserRoles {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String TECHNOLOGY_EMPLOYEE = "ROLE_TECHNOLOGY_EMPLOYEE";
        public static final String QUALITY_EMPLOYEE = "ROLE_QUALITY_EMPLOYEE";
        public static final String PURCHASE_EMPLOYEE = "ROLE_PURCHASE_EMPLOYEE";
        public static final String ASSEMBLY_EMPLOYEE = "ROLE_ASSEMBLY_EMPLOYEE";
        public static final String PRODUCE_EMPLOYEE = "ROLE_PRODUCE_EMPLOYEE";
        public static final String GUARANTEE_EMPLOYEE = "ROLE_GUARANTEE_EMPLOYEE";
    }

    public static final class Material {
        public static final String STRUCTURE_NO = "structureNo";
        public static final String EXPORT_STATUS = "exportStatus";
        public static final String SOURCE_MARK = "sourceMark";
        public static final String TECHNOLOGY_STATUS = "technologyStatus";
        public static final String QUALITY_STATUS = "qualityStatus";
        public static final String PURCHASE_STATUS = "purchaseStatus";
        public static final String ASSEMBLY_STATUS = "assemblyStatus";
        public static final String PRODUCE_STATUS = "produceStatus";

        public static final class ExportStatus {
            public static final int IMPERFECT = 0;
            public static final int EXPORTABLE = 1;
            public static final int EXPORTED = 2;
        }

        public static final class PerfectStatus {
            public static final int IMPERFECT = 0;
            public static final int PERFECTED = 1;
        }
    }

    public static final class Status {
        public static final int DELETED = 0;
        public static final int GENERAL = 1;
    }
}
