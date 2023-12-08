-- ----------------------------
-- Table structure for sys_attachment
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_attachment";
CREATE TABLE "public"."sys_attachment"
(
    "id"           int8 NOT NULL,
    "created_by"   int8,
    "created_time" timestamp(6),
    "updated_by"   int8,
    "updated_time" timestamp(6),
    "is_deleted"   int2,
    "version"      int4,
    "tenant_id"    int8,
    "filename"     varchar(64) COLLATE "pg_catalog"."default",
    "filepath"     varchar(255) COLLATE "pg_catalog"."default",
    "key"          varchar(64) COLLATE "pg_catalog"."default",
    "provider"     varchar(64) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."sys_attachment" OWNER TO "postgres";
COMMENT
ON COLUMN "public"."sys_attachment"."id" IS '主键';
COMMENT
ON COLUMN "public"."sys_attachment"."created_by" IS '创建人';
COMMENT
ON COLUMN "public"."sys_attachment"."created_time" IS '创建时间';
COMMENT
ON COLUMN "public"."sys_attachment"."updated_by" IS '更新人';
COMMENT
ON COLUMN "public"."sys_attachment"."updated_time" IS '更新时间';
COMMENT
ON COLUMN "public"."sys_attachment"."is_deleted" IS '逻辑删除';
COMMENT
ON COLUMN "public"."sys_attachment"."version" IS '版本号';
COMMENT
ON COLUMN "public"."sys_attachment"."tenant_id" IS '租户号';
COMMENT
ON COLUMN "public"."sys_attachment"."filename" IS '文件名称';
COMMENT
ON COLUMN "public"."sys_attachment"."filepath" IS '文件地址';
COMMENT
ON COLUMN "public"."sys_attachment"."key" IS '关键附件标识';
COMMENT
ON COLUMN "public"."sys_attachment"."provider" IS '存储供应商';
COMMENT
ON TABLE "public"."sys_attachment" IS '系统附件';

-- ----------------------------
-- Records of sys_attachment
-- ----------------------------
BEGIN;
INSERT INTO "public"."sys_attachment" ("id", "created_by", "created_time", "updated_by", "updated_time", "is_deleted",
                                       "version", "tenant_id", "filename", "filepath", "key", "provider")
VALUES (1175490443056513024, 1166010721390034944, '2023-11-18 17:39:21.184', 1166010721390034944,
        '2023-11-18 17:39:21.185', 0, NULL, 0, 'file', 'file', 'file', 'ALIYUN');
INSERT INTO "public"."sys_attachment" ("id", "created_by", "created_time", "updated_by", "updated_time", "is_deleted",
                                       "version", "tenant_id", "filename", "filepath", "key", "provider")
VALUES (1175496962619736064, 1166010721390034944, '2023-11-18 18:05:15.594', 1166010721390034944,
        '2023-11-18 19:16:47.183', 0, NULL, 0, '1700274482216.jpg',
        'http://127.0.0.1:8600/sys/attachment/download/1700274482216.jpg', 'file', 'ALIYUN');
INSERT INTO "public"."sys_attachment" ("id", "created_by", "created_time", "updated_by", "updated_time", "is_deleted",
                                       "version", "tenant_id", "filename", "filepath", "key", "provider")
VALUES (1176144111364210688, 1166010721390034944, '2023-11-20 12:56:47.873', 1166010721390034944,
        '2023-11-20 12:56:47.873', 0, NULL, 0, 'file',
        'http://127.0.0.1:8600/sys/attachment/download/1700274482216 (2).jpg', 'file', 'ALIYUN');
INSERT INTO "public"."sys_attachment" ("id", "created_by", "created_time", "updated_by", "updated_time", "is_deleted",
                                       "version", "tenant_id", "filename", "filepath", "key", "provider")
VALUES (1176144203441242112, 1166010721390034944, '2023-11-20 12:57:09.835', 1166010721390034944,
        '2023-11-20 12:57:17.742', 0, NULL, 0, '1700274482216 (2).jpg',
        'http://127.0.0.1:8600/sys/attachment/download/1700274482216 (2).jpg', '1700274482216 (2).jpg', 'ALIYUN');
INSERT INTO "public"."sys_attachment" ("id", "created_by", "created_time", "updated_by", "updated_time", "is_deleted",
                                       "version", "tenant_id", "filename", "filepath", "key", "provider")
VALUES (1176144262031605760, 1166010721390034944, '2023-11-20 12:57:23.777', 1166010721390034944,
        '2023-11-20 12:57:23.777', 0, NULL, 0, '1700274482216.jpg',
        'http://127.0.0.1:8600/sys/attachment/download/1700274482216.jpg', '1700274482216.jpg', 'ALIYUN');
INSERT INTO "public"."sys_attachment" ("id", "created_by", "created_time", "updated_by", "updated_time", "is_deleted",
                                       "version", "tenant_id", "filename", "filepath", "key", "provider")
VALUES (1176144303366602752, 1166010721390034944, '2023-11-20 12:57:33.629', 1166010721390034944,
        '2023-11-20 12:57:33.629', 0, NULL, 0, '1700274482216 (1).jpg',
        'http://127.0.0.1:8600/sys/attachment/download/1700274482216 (1).jpg', '1700274482216 (1).jpg', 'ALIYUN');
INSERT INTO "public"."sys_attachment" ("id", "created_by", "created_time", "updated_by", "updated_time", "is_deleted",
                                       "version", "tenant_id", "filename", "filepath", "key", "provider")
VALUES (1176158242679422976, 1166010721390034944, '2023-11-20 13:52:57.024', 1180483526638698496,
        '2023-12-02 13:11:31.895', 0, NULL, 0, '113543189_p0_master1200.jpg',
        'http://127.0.0.1:8600/sys/attachment/download/113543189_p0_master1200.jpg', '113543189_p0_master1200.jpg',
        'ALIYUN');
COMMIT;

-- ----------------------------
-- Table structure for sys_cloud_storage_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_cloud_storage_config";
CREATE TABLE "public"."sys_cloud_storage_config"
(
    "id"           int8 NOT NULL,
    "name"         varchar(64) COLLATE "pg_catalog"."default",
    "endpoint"     varchar(64) COLLATE "pg_catalog"."default",
    "provider"     varchar(64) COLLATE "pg_catalog"."default",
    "cs_type"      varchar(64) COLLATE "pg_catalog"."default",
    "bucket"       varchar(255) COLLATE "pg_catalog"."default",
    "access_id"    varchar(64) COLLATE "pg_catalog"."default",
    "access_key"   varchar(64) COLLATE "pg_catalog"."default",
    "enable"       varchar(12) COLLATE "pg_catalog"."default",
    "created_by"   int8,
    "created_time" timestamp(6),
    "updated_by"   int8,
    "updated_time" timestamp(6),
    "is_deleted"   int2,
    "version"      int4
)
;
ALTER TABLE "public"."sys_cloud_storage_config" OWNER TO "postgres";
COMMENT
ON COLUMN "public"."sys_cloud_storage_config"."id" IS '主键';
COMMENT
ON COLUMN "public"."sys_cloud_storage_config"."name" IS '云存储配置名称';
COMMENT
ON COLUMN "public"."sys_cloud_storage_config"."endpoint" IS '云存储端点';
COMMENT
ON COLUMN "public"."sys_cloud_storage_config"."provider" IS '云存储供应商';
COMMENT
ON COLUMN "public"."sys_cloud_storage_config"."cs_type" IS '云存储类型';
COMMENT
ON COLUMN "public"."sys_cloud_storage_config"."bucket" IS '存储空间';
COMMENT
ON COLUMN "public"."sys_cloud_storage_config"."access_id" IS '访问ID';
COMMENT
ON COLUMN "public"."sys_cloud_storage_config"."access_key" IS '访问密钥';
COMMENT
ON COLUMN "public"."sys_cloud_storage_config"."enable" IS '是否启用';
COMMENT
ON COLUMN "public"."sys_cloud_storage_config"."created_by" IS '创建人';
COMMENT
ON COLUMN "public"."sys_cloud_storage_config"."created_time" IS '创建时间';
COMMENT
ON COLUMN "public"."sys_cloud_storage_config"."updated_by" IS '更新人';
COMMENT
ON COLUMN "public"."sys_cloud_storage_config"."updated_time" IS '更新时间';
COMMENT
ON COLUMN "public"."sys_cloud_storage_config"."is_deleted" IS '逻辑删除';
COMMENT
ON COLUMN "public"."sys_cloud_storage_config"."version" IS '版本号';
COMMENT
ON TABLE "public"."sys_cloud_storage_config" IS '系统云存储配置';

-- ----------------------------
-- Records of sys_cloud_storage_config
-- ----------------------------
BEGIN;
INSERT INTO "public"."sys_cloud_storage_config" ("id", "name", "endpoint", "provider", "cs_type", "bucket", "access_id",
                                                 "access_key", "enable", "created_by", "created_time", "updated_by",
                                                 "updated_time", "is_deleted", "version")
VALUES (1175146183048298496, '阿里云', 'https://oss-cn-beijing.aliyuncs.com/', 'ALIYUN', 'OSS', 'uno-test',
        'LTAI5tH6b6JdvpRSicv9DP9H', 'LhUC6MCxRGH13ub1ipHxWXYwJp0dVF', 'ENABLE', 1166010721390034944,
        '2023-11-17 18:51:23.2', 1166010721390034944, '2023-11-17 18:57:10.672', 0, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_dic
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dic";
CREATE TABLE "public"."sys_dic"
(
    "id"           int8 NOT NULL,
    "parent_id"    int8,
    "type"         varchar(64) COLLATE "pg_catalog"."default",
    "code"         varchar(64) COLLATE "pg_catalog"."default",
    "name"         varchar(64) COLLATE "pg_catalog"."default",
    "des"          varchar(1000) COLLATE "pg_catalog"."default",
    "sort"         int4 DEFAULT 0,
    "color"        varchar(64) COLLATE "pg_catalog"."default",
    "created_by"   int8,
    "created_time" timestamp(6),
    "updated_by"   int8,
    "updated_time" timestamp(6),
    "is_deleted"   int2,
    "version"      int4,
    "tenant_id"    int8
)
;
ALTER TABLE "public"."sys_dic" OWNER TO "postgres";
COMMENT
ON COLUMN "public"."sys_dic"."id" IS '主键';
COMMENT
ON COLUMN "public"."sys_dic"."parent_id" IS '父级';
COMMENT
ON COLUMN "public"."sys_dic"."type" IS '字典类型;system 系统类 biz 业务类';
COMMENT
ON COLUMN "public"."sys_dic"."code" IS '字典编码';
COMMENT
ON COLUMN "public"."sys_dic"."name" IS '字典名称';
COMMENT
ON COLUMN "public"."sys_dic"."des" IS '字典描述';
COMMENT
ON COLUMN "public"."sys_dic"."sort" IS '排序';
COMMENT
ON COLUMN "public"."sys_dic"."color" IS '颜色';
COMMENT
ON COLUMN "public"."sys_dic"."created_by" IS '创建人';
COMMENT
ON COLUMN "public"."sys_dic"."created_time" IS '创建时间';
COMMENT
ON COLUMN "public"."sys_dic"."updated_by" IS '更新人';
COMMENT
ON COLUMN "public"."sys_dic"."updated_time" IS '更新时间';
COMMENT
ON COLUMN "public"."sys_dic"."is_deleted" IS '逻辑删除';
COMMENT
ON COLUMN "public"."sys_dic"."version" IS '版本号';
COMMENT
ON COLUMN "public"."sys_dic"."tenant_id" IS '租户号';
COMMENT
ON TABLE "public"."sys_dic" IS '字典管理';

-- ----------------------------
-- Records of sys_dic
-- ----------------------------
BEGIN;
INSERT INTO "public"."sys_dic" ("id", "parent_id", "type", "code", "name", "des", "sort", "color", "created_by",
                                "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1178021293245726720, NULL, 'SYSTEM', 'code2', '字典2', NULL, 0, NULL, 1166010721390034944,
        '2023-11-25 17:16:02.891', 1166010721390034944, '2023-11-25 17:18:11.854', 1, NULL, 0);
INSERT INTO "public"."sys_dic" ("id", "parent_id", "type", "code", "name", "des", "sort", "color", "created_by",
                                "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1178022261165391872, NULL, 'SYSTEM', 'code2', '字典2', NULL, 0, NULL, 1166010721390034944,
        '2023-11-25 17:19:53.66', 1166010721390034944, '2023-11-25 17:19:56.056', 1, NULL, 0);
INSERT INTO "public"."sys_dic" ("id", "parent_id", "type", "code", "name", "des", "sort", "color", "created_by",
                                "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1178020895659130880, NULL, 'SYSTEM', 'code1', '字典1', NULL, 0, NULL, 1166010721390034944,
        '2023-11-25 17:14:28.098', 1166010721390034944, '2023-11-25 17:20:00.559', 1, NULL, 0);
INSERT INTO "public"."sys_dic" ("id", "parent_id", "type", "code", "name", "des", "sort", "color", "created_by",
                                "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1178022323111198720, NULL, 'BIZ', 'code1', '字典1', NULL, 0, NULL, 1166010721390034944,
        '2023-11-25 17:20:08.428', 1166010721390034944, '2023-11-25 17:29:20.766', 1, NULL, 0);
INSERT INTO "public"."sys_dic" ("id", "parent_id", "type", "code", "name", "des", "sort", "color", "created_by",
                                "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1178028928863174656, 1178026484078608384, 'BIZ', 'zxc', 'zxc', NULL, 0, NULL, 1166010721390034944,
        '2023-11-25 17:46:23.363', 1166010721390034944, '2023-11-25 18:07:55.481', 1, NULL, 0);
INSERT INTO "public"."sys_dic" ("id", "parent_id", "type", "code", "name", "des", "sort", "color", "created_by",
                                "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1178034971492220928, 1178026484078608384, 'BIZ', 'zxc', 'asdxzc', NULL, 0, NULL, 1166010721390034944,
        '2023-11-25 18:10:24.039', 1166010721390034944, '2023-11-27 10:11:11.281', 1, NULL, 0);
INSERT INTO "public"."sys_dic" ("id", "parent_id", "type", "code", "name", "des", "sort", "color", "created_by",
                                "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1178663847033831424, 1178026484078608384, 'BIZ', '123', '123', NULL, 0, 'green', 1166010721390034944,
        '2023-11-27 11:49:19.649', 1166010721390034944, '2023-11-27 11:54:29.178', 1, NULL, 0);
INSERT INTO "public"."sys_dic" ("id", "parent_id", "type", "code", "name", "des", "sort", "color", "created_by",
                                "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1178026767840182272, NULL, 'SYSTEM', 'asd', 'asd', NULL, 0, NULL, 1166010721390034944,
        '2023-11-25 17:37:48.134', 1166010721390034944, '2023-11-27 11:54:31.697', 1, NULL, 0);
INSERT INTO "public"."sys_dic" ("id", "parent_id", "type", "code", "name", "des", "sort", "color", "created_by",
                                "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1178026371738238976, NULL, 'SYSTEM', 'code1', '字典1', NULL, 0, NULL, 1166010721390034944,
        '2023-11-25 17:36:13.694', 1166010721390034944, '2023-11-27 11:54:33.31', 1, NULL, 0);
INSERT INTO "public"."sys_dic" ("id", "parent_id", "type", "code", "name", "des", "sort", "color", "created_by",
                                "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1178026484078608384, NULL, 'SYSTEM', 'code2', '字典2', NULL, 0, NULL, 1166010721390034944,
        '2023-11-25 17:36:40.483', 1166010721390034944, '2023-11-27 11:54:34.577', 1, NULL, 0);
INSERT INTO "public"."sys_dic" ("id", "parent_id", "type", "code", "name", "des", "sort", "color", "created_by",
                                "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1178665211281670144, NULL, 'BIZ', '123', '123', NULL, 0, NULL, 1166010721390034944, '2023-11-27 11:54:44.913',
        1166010721390034944, '2023-11-27 11:54:48.745', 1, NULL, 0);
INSERT INTO "public"."sys_dic" ("id", "parent_id", "type", "code", "name", "des", "sort", "color", "created_by",
                                "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1178730661403426816, NULL, 'SYSTEM', '213', '123', NULL, 0, NULL, 1166010721390034944,
        '2023-11-27 16:14:49.437', 1166010721390034944, '2023-11-27 16:14:53.256', 1, NULL, 0);
INSERT INTO "public"."sys_dic" ("id", "parent_id", "type", "code", "name", "des", "sort", "color", "created_by",
                                "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1178731158910926848, NULL, 'SYSTEM', '123', '123', NULL, 0, NULL, 1166010721390034944,
        '2023-11-27 16:16:48.062', 1166010721390034944, '2023-11-27 16:16:56.228', 1, NULL, 0);
INSERT INTO "public"."sys_dic" ("id", "parent_id", "type", "code", "name", "des", "sort", "color", "created_by",
                                "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1178731392059834368, NULL, 'SYSTEM', 'asd', 'asd', NULL, 0, NULL, 1166010721390034944,
        '2023-11-27 16:17:43.637', 1166010721390034944, '2023-11-27 16:17:54.829', 1, NULL, 0);
INSERT INTO "public"."sys_dic" ("id", "parent_id", "type", "code", "name", "des", "sort", "color", "created_by",
                                "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1178731626458644480, NULL, 'BIZ', '123', '123', NULL, 0, NULL, 1166010721390034944, '2023-11-27 16:18:39.523',
        1166010721390034944, '2023-11-27 16:18:41.887', 1, NULL, 0);
INSERT INTO "public"."sys_dic" ("id", "parent_id", "type", "code", "name", "des", "sort", "color", "created_by",
                                "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1178743017515778048, NULL, 'SYSTEM', 'org', '组织类型', NULL, 0, NULL, 1166010721390034944,
        '2023-11-27 17:03:55.363', 1166010721390034944, '2023-11-27 17:03:55.242', 0, NULL, 0);
INSERT INTO "public"."sys_dic" ("id", "parent_id", "type", "code", "name", "des", "sort", "color", "created_by",
                                "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1178743108578443264, 1178743017515778048, 'SYSTEM', 'department', '部门', NULL, 0, 'amber', 1166010721390034944,
        '2023-11-27 17:04:17.077', 1166010721390034944, '2023-11-27 17:04:16.964', 0, NULL, 0);
INSERT INTO "public"."sys_dic" ("id", "parent_id", "type", "code", "name", "des", "sort", "color", "created_by",
                                "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1178743176350138368, 1178743017515778048, 'SYSTEM', 'user_group', '用户组', NULL, 0, 'cyan',
        1166010721390034944, '2023-11-27 17:04:33.234', 1166010721390034944, '2023-11-27 17:04:33.12', 0, NULL, 0);
INSERT INTO "public"."sys_dic" ("id", "parent_id", "type", "code", "name", "des", "sort", "color", "created_by",
                                "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1179846041990987776, NULL, 'SYSTEM', 'test', '测试', NULL, 0, NULL, 1166010721390034944,
        '2023-11-30 18:06:56.89', 1166010721390034944, '2023-11-30 18:06:56.797', 0, NULL, 0);
INSERT INTO "public"."sys_dic" ("id", "parent_id", "type", "code", "name", "des", "sort", "color", "created_by",
                                "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1180174973447045120, 1178743017515778048, 'SYSTEM', 'company', '公司', NULL, 0, 'blue', 1166010721390034944,
        '2023-12-01 15:54:00', 1166010721390034944, '2023-12-01 15:54:54.608', 0, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_menu";
CREATE TABLE "public"."sys_menu"
(
    "id"           int8 NOT NULL,
    "name"         varchar(64) COLLATE "pg_catalog"."default",
    "sort"         int4 DEFAULT 0,
    "parent_id"    int8,
    "alias"        varchar(64) COLLATE "pg_catalog"."default",
    "route"        varchar(64) COLLATE "pg_catalog"."default",
    "icon"         varchar(64) COLLATE "pg_catalog"."default",
    "created_by"   int8,
    "created_time" timestamp(6),
    "updated_by"   int8,
    "updated_time" timestamp(6),
    "is_deleted"   int2,
    "version"      int8 DEFAULT 0,
    "tenant_id"    int8,
    "code"         varchar(64) COLLATE "pg_catalog"."default",
    "type"         varchar(64) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."sys_menu" OWNER TO "postgres";
COMMENT
ON COLUMN "public"."sys_menu"."id" IS '主键';
COMMENT
ON COLUMN "public"."sys_menu"."name" IS '菜单名称';
COMMENT
ON COLUMN "public"."sys_menu"."sort" IS '菜单序号';
COMMENT
ON COLUMN "public"."sys_menu"."parent_id" IS '父级菜单id';
COMMENT
ON COLUMN "public"."sys_menu"."alias" IS '别名';
COMMENT
ON COLUMN "public"."sys_menu"."route" IS '菜单路径';
COMMENT
ON COLUMN "public"."sys_menu"."icon" IS 'icon';
COMMENT
ON COLUMN "public"."sys_menu"."created_by" IS '创建人';
COMMENT
ON COLUMN "public"."sys_menu"."created_time" IS '创建时间';
COMMENT
ON COLUMN "public"."sys_menu"."updated_by" IS '更新人';
COMMENT
ON COLUMN "public"."sys_menu"."updated_time" IS '更新时间';
COMMENT
ON COLUMN "public"."sys_menu"."is_deleted" IS '逻辑删除';
COMMENT
ON COLUMN "public"."sys_menu"."version" IS '版本号';
COMMENT
ON COLUMN "public"."sys_menu"."tenant_id" IS '租户号';
COMMENT
ON COLUMN "public"."sys_menu"."code" IS '菜单编码';
COMMENT
ON COLUMN "public"."sys_menu"."type" IS '菜单类型';
COMMENT
ON TABLE "public"."sys_menu" IS '系统菜单';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO "public"."sys_menu" ("id", "name", "sort", "parent_id", "alias", "route", "icon", "created_by",
                                 "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id",
                                 "code", "type")
VALUES (1180149758279680000, '岗位管理', 1, 1172187214407270400, '岗位管理', '/system/post', 'IconBottomCenterStroked',
        1166010721390034944, '2023-12-01 14:13:48', 1166010721390034944, '2023-12-03 16:20:52.618', 0, 1, 0, 'post',
        'MENU');
INSERT INTO "public"."sys_menu" ("id", "name", "sort", "parent_id", "alias", "route", "icon", "created_by",
                                 "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id",
                                 "code", "type")
VALUES (1176519196318629888, '菜单管理-[添加]', 0, 1176519196318629888, 'menu_add', NULL, NULL, 1166010721390034944,
        '2023-11-21 13:47:15.072', 1166010721390034944, '2023-11-21 13:47:25.68', 0, 0, 0, 'menu_add', 'BUTTON');
INSERT INTO "public"."sys_menu" ("id", "name", "sort", "parent_id", "alias", "route", "icon", "created_by",
                                 "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id",
                                 "code", "type")
VALUES (1176519513181650944, '菜单管理-[添加]', 0, 1172187370985095168, 'menu_add', NULL, NULL, 1166010721390034944,
        '2023-11-21 13:48:30.621', 1166010721390034944, '2023-11-21 13:48:59.03', 0, 0, 0, 'menu_add', 'BUTTON');
INSERT INTO "public"."sys_menu" ("id", "name", "sort", "parent_id", "alias", "route", "icon", "created_by",
                                 "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id",
                                 "code", "type")
VALUES (1175513370862813184, '附件管理', 1, 1172187214407270400, '附件管理', '/system/attachment', 'IconBriefStroked',
        1166010721390034944, '2023-11-18 19:10:27', 1166010721390034944, '2023-12-03 16:20:56.09', 0, 2, 0,
        'attachment', 'MENU');
INSERT INTO "public"."sys_menu" ("id", "name", "sort", "parent_id", "alias", "route", "icon", "created_by",
                                 "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id",
                                 "code", "type")
VALUES (1180906374104743936, '测试第二层子级', 0, 1180906265245646848, NULL, NULL, NULL, 1166010721390034944,
        '2023-12-03 16:20:19.768', 1166010721390034944, '2023-12-04 20:17:00.639', 1, 0, 0, 'second_child', 'MENU');
INSERT INTO "public"."sys_menu" ("id", "name", "sort", "parent_id", "alias", "route", "icon", "created_by",
                                 "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id",
                                 "code", "type")
VALUES (1180906265245646848, '测试', 0, 1172187214407270400, NULL, NULL, NULL, 1166010721390034944,
        '2023-12-03 16:19:53.813', 1166010721390034944, '2023-12-04 20:17:03.697', 1, 0, 0, 'test', 'MENU');
INSERT INTO "public"."sys_menu" ("id", "name", "sort", "parent_id", "alias", "route", "icon", "created_by",
                                 "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id",
                                 "code", "type")
VALUES (1172187214407270400, '系统管理', 1, NULL, '系统管理', '', 'IconSearch', 1166010721390034944,
        '2023-11-09 14:53:30.103', 1166010721390034944, '2023-11-15 13:37:14.013', 0, NULL, 0, 'system', 'MENU');
INSERT INTO "public"."sys_menu" ("id", "name", "sort", "parent_id", "alias", "route", "icon", "created_by",
                                 "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id",
                                 "code", "type")
VALUES (1176525950863802368, '图标管理', 0, 1175140969742729216, '图标管理', '/develop/icon', 'IconFaceuLogo',
        1166010721390034944, '2023-11-21 14:14:05.481', 1166010721390034944, '2023-11-24 12:09:40.779', 0, 0, 0, 'icon',
        'MENU');
INSERT INTO "public"."sys_menu" ("id", "name", "sort", "parent_id", "alias", "route", "icon", "created_by",
                                 "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id",
                                 "code", "type")
VALUES (1175141121530527744, '云存储配置', 1, 1175140969742729216, '云存储配置', '/develop/cloudstorageconfig',
        'IconAbsoluteStroked', 1166010721390034944, '2023-11-17 18:31:16.443', 1166010721390034944,
        '2023-11-24 12:09:47.14', 0, 0, 0, 'cloudstorageconfig', 'MENU');
INSERT INTO "public"."sys_menu" ("id", "name", "sort", "parent_id", "alias", "route", "icon", "created_by",
                                 "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id",
                                 "code", "type")
VALUES (1172187370985095168, '菜单管理', 1, 1172187214407270400, '菜单管理', '/system/menu', 'IconVigoLogo',
        1166010721390034944, '2023-11-09 14:54:07.433', 1166010721390034944, '2023-11-24 12:09:55.537', 0, NULL, 0,
        'menu', 'MENU');
INSERT INTO "public"."sys_menu" ("id", "name", "sort", "parent_id", "alias", "route", "icon", "created_by",
                                 "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id",
                                 "code", "type")
VALUES (1172187336839135232, '角色管理', 1, 1172187214407270400, '角色管理', '/system/role', 'IconShoppingBag',
        1166010721390034944, '2023-11-09 14:53:59.293', 1166010721390034944, '2023-11-24 12:10:00.901', 0, NULL, 0,
        'role', 'MENU');
INSERT INTO "public"."sys_menu" ("id", "name", "sort", "parent_id", "alias", "route", "icon", "created_by",
                                 "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id",
                                 "code", "type")
VALUES (1177302820223713280, '字典管理', 1, 1172187214407270400, '字典管理', '/system/dictionary',
        'IconCandlestickChartStroked', 1166010721390034944, '2023-11-23 17:41:05.58', 1166010721390034944,
        '2023-11-24 12:10:08.785', 0, 0, 0, 'dic', 'MENU');
INSERT INTO "public"."sys_menu" ("id", "name", "sort", "parent_id", "alias", "route", "icon", "created_by",
                                 "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id",
                                 "code", "type")
VALUES (1176547780762533888, '组织管理', 2, 1172187214407270400, '组织管理', '/system/org', 'IconCoinMoneyStroked',
        1166010721390034944, '2023-11-21 15:40:50.135', 1166010721390034944, '2023-11-24 12:10:16.025', 0, 0, 0, 'org',
        'MENU');
INSERT INTO "public"."sys_menu" ("id", "name", "sort", "parent_id", "alias", "route", "icon", "created_by",
                                 "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id",
                                 "code", "type")
VALUES (1175140969742729216, '开发管理', 1, NULL, '开发管理', NULL, 'IconAlignVCenterStroked', 1166010721390034944,
        '2023-11-17 18:30:40.253', 1166010721390034944, '2023-11-27 15:24:01.67', 0, 1, 0, 'developer', 'MENU');
INSERT INTO "public"."sys_menu" ("id", "name", "sort", "parent_id", "alias", "route", "icon", "created_by",
                                 "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id",
                                 "code", "type")
VALUES (1179111365425233920, '测试', 1, 1177303142178619392, '123', NULL, NULL, 1166010721390034944,
        '2023-11-28 17:27:36.35', 1166010721390034944, '2023-11-28 17:29:00.79', 1, 0, 0, '123', 'MENU');
INSERT INTO "public"."sys_menu" ("id", "name", "sort", "parent_id", "alias", "route", "icon", "created_by",
                                 "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id",
                                 "code", "type")
VALUES (1179124112140795904, '租户管理', 1, 1172187214407270400, '租户管理', '/system/tenant',
        'IconAlignHCenterStroked', 1166010721390034944, '2023-11-28 18:18:15.402', 1166010721390034944,
        '2023-11-28 18:23:59.732', 0, 2, 0, 'tenant', 'MENU');
INSERT INTO "public"."sys_menu" ("id", "name", "sort", "parent_id", "alias", "route", "icon", "created_by",
                                 "created_time", "updated_by", "updated_time", "is_deleted", "version", "tenant_id",
                                 "code", "type")
VALUES (1177303142178619392, '用户管理', 5, 1172187214407270400, '用户管理', '/system/user', 'IconLikeThumb',
        1166010721390034944, '2023-11-23 17:42:22.334', 1166010721390034944, '2023-11-28 18:30:44.288', 0, 1, 0, 'user',
        'MENU');
COMMIT;

-- ----------------------------
-- Table structure for sys_org
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_org";
CREATE TABLE "public"."sys_org"
(
    "id"           int8 NOT NULL,
    "parent_id"    int8,
    "code"         varchar(64) COLLATE "pg_catalog"."default",
    "name"         varchar(64) COLLATE "pg_catalog"."default",
    "des"          varchar(1024) COLLATE "pg_catalog"."default",
    "sort"         int4 DEFAULT 0,
    "type"         varchar(64) COLLATE "pg_catalog"."default",
    "created_by"   int8,
    "created_time" timestamp(6),
    "updated_by"   int8,
    "updated_time" timestamp(6),
    "is_deleted"   int2,
    "version"      int4,
    "tenant_id"    int8
)
;
ALTER TABLE "public"."sys_org" OWNER TO "postgres";
COMMENT
ON COLUMN "public"."sys_org"."id" IS '主键';
COMMENT
ON COLUMN "public"."sys_org"."parent_id" IS '父级';
COMMENT
ON COLUMN "public"."sys_org"."code" IS '编码';
COMMENT
ON COLUMN "public"."sys_org"."name" IS '名称';
COMMENT
ON COLUMN "public"."sys_org"."des" IS '描述';
COMMENT
ON COLUMN "public"."sys_org"."sort" IS '排序';
COMMENT
ON COLUMN "public"."sys_org"."type" IS '组织类型;部门、小组...';
COMMENT
ON COLUMN "public"."sys_org"."created_by" IS '创建人';
COMMENT
ON COLUMN "public"."sys_org"."created_time" IS '创建时间';
COMMENT
ON COLUMN "public"."sys_org"."updated_by" IS '更新人';
COMMENT
ON COLUMN "public"."sys_org"."updated_time" IS '更新时间';
COMMENT
ON COLUMN "public"."sys_org"."is_deleted" IS '逻辑删除';
COMMENT
ON COLUMN "public"."sys_org"."version" IS '版本号';
COMMENT
ON COLUMN "public"."sys_org"."tenant_id" IS '租户号';
COMMENT
ON TABLE "public"."sys_org" IS '系统组织';

-- ----------------------------
-- Records of sys_org
-- ----------------------------
BEGIN;
INSERT INTO "public"."sys_org" ("id", "parent_id", "code", "name", "des", "sort", "type", "created_by", "created_time",
                                "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1179129791754403840, 1179108978689966080, 'g1', '小组1', NULL, 0, 'user_group', 1166010721390034944,
        '2023-11-28 18:40:49.529', 1166010721390034944, '2023-11-28 18:40:49.41', 0, NULL, 0);
INSERT INTO "public"."sys_org" ("id", "parent_id", "code", "name", "des", "sort", "type", "created_by", "created_time",
                                "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1180175055613591552, NULL, 'ClearX', 'ClearX', NULL, 0, 'company', 1166010721390034944,
        '2023-12-01 15:54:19.851', 1166010721390034944, '2023-12-01 15:54:19.733', 0, NULL, 0);
INSERT INTO "public"."sys_org" ("id", "parent_id", "code", "name", "des", "sort", "type", "created_by", "created_time",
                                "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1180175322287570944, 1180175055613591552, 'Devloper', '研发部', NULL, 0, 'department', 1166010721390034944,
        '2023-12-01 15:55:23.435', 1166010721390034944, '2023-12-01 15:55:23.333', 0, NULL, 0);
INSERT INTO "public"."sys_org" ("id", "parent_id", "code", "name", "des", "sort", "type", "created_by", "created_time",
                                "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1180175387064532992, 1180175322287570944, 'Turbo', 'Turbo小组', NULL, 0, 'user_group', 1166010721390034944,
        '2023-12-01 15:55:38.877', 1166010721390034944, '2023-12-01 15:55:38.763', 0, NULL, 0);
INSERT INTO "public"."sys_org" ("id", "parent_id", "code", "name", "des", "sort", "type", "created_by", "created_time",
                                "updated_by", "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1179108978689966080, NULL, 'test', '测试组织', '123', 0, 'department', 1166010721390034944,
        '2023-11-28 17:18:07', 1166010721390034944, '2023-12-01 15:55:49.113', 0, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_post";
CREATE TABLE "public"."sys_post"
(
    "id"           int8 NOT NULL,
    "code"         varchar(64) COLLATE "pg_catalog"."default",
    "name"         varchar(64) COLLATE "pg_catalog"."default",
    "sort"         int4 DEFAULT 0,
    "des"          text COLLATE "pg_catalog"."default",
    "created_by"   int8,
    "created_time" timestamp(6),
    "updated_by"   int8,
    "updated_time" timestamp(6),
    "is_deleted"   int2,
    "version"      int4,
    "tenant_id"    int8
)
;
ALTER TABLE "public"."sys_post" OWNER TO "postgres";
COMMENT
ON COLUMN "public"."sys_post"."id" IS '主键';
COMMENT
ON COLUMN "public"."sys_post"."code" IS '岗位编码';
COMMENT
ON COLUMN "public"."sys_post"."name" IS '岗位名称';
COMMENT
ON COLUMN "public"."sys_post"."sort" IS '岗位序号';
COMMENT
ON COLUMN "public"."sys_post"."des" IS '岗位描述';
COMMENT
ON COLUMN "public"."sys_post"."created_by" IS '创建人';
COMMENT
ON COLUMN "public"."sys_post"."created_time" IS '创建时间';
COMMENT
ON COLUMN "public"."sys_post"."updated_by" IS '更新人';
COMMENT
ON COLUMN "public"."sys_post"."updated_time" IS '更新时间';
COMMENT
ON COLUMN "public"."sys_post"."is_deleted" IS '逻辑删除';
COMMENT
ON COLUMN "public"."sys_post"."version" IS '版本号';
COMMENT
ON COLUMN "public"."sys_post"."tenant_id" IS '租户号';
COMMENT
ON TABLE "public"."sys_post" IS '系统岗位';

-- ----------------------------
-- Records of sys_post
-- ----------------------------
BEGIN;
INSERT INTO "public"."sys_post" ("id", "code", "name", "sort", "des", "created_by", "created_time", "updated_by",
                                 "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1180165158766379008, 'architecture', '架构', 0, NULL, 1166010721390034944, '2023-12-01 15:15:00.258',
        1166010721390034944, '2023-12-01 15:15:00.128', 0, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role";
CREATE TABLE "public"."sys_role"
(
    "id"           int8 NOT NULL,
    "name"         varchar(64) COLLATE "pg_catalog"."default",
    "code"         varchar(64) COLLATE "pg_catalog"."default",
    "des"          varchar(255) COLLATE "pg_catalog"."default",
    "sort"         int4,
    "created_by"   int8,
    "created_time" timestamp(6),
    "updated_by"   int8,
    "updated_time" timestamp(6),
    "is_deleted"   int2,
    "version"      int4 DEFAULT 0,
    "tenant_id"    int8
)
;
ALTER TABLE "public"."sys_role" OWNER TO "postgres";
COMMENT
ON COLUMN "public"."sys_role"."id" IS '主键';
COMMENT
ON COLUMN "public"."sys_role"."name" IS '角色名称';
COMMENT
ON COLUMN "public"."sys_role"."code" IS '角色编码';
COMMENT
ON COLUMN "public"."sys_role"."des" IS '描述';
COMMENT
ON COLUMN "public"."sys_role"."sort" IS '排序';
COMMENT
ON COLUMN "public"."sys_role"."created_by" IS '创建人';
COMMENT
ON COLUMN "public"."sys_role"."created_time" IS '创建时间';
COMMENT
ON COLUMN "public"."sys_role"."updated_by" IS '更新人';
COMMENT
ON COLUMN "public"."sys_role"."updated_time" IS '更新时间';
COMMENT
ON COLUMN "public"."sys_role"."is_deleted" IS '逻辑删除';
COMMENT
ON COLUMN "public"."sys_role"."version" IS '版本号';
COMMENT
ON COLUMN "public"."sys_role"."tenant_id" IS '租户号';
COMMENT
ON TABLE "public"."sys_role" IS '系统角色';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO "public"."sys_role" ("id", "name", "code", "des", "sort", "created_by", "created_time", "updated_by",
                                 "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1174045373997121536, 'asd', 'asd', NULL, NULL, 1166010721390034944, '2023-11-14 17:57:07.887',
        1166010721390034944, '2023-11-21 11:13:11.12', 1, 1, 0);
INSERT INTO "public"."sys_role" ("id", "name", "code", "des", "sort", "created_by", "created_time", "updated_by",
                                 "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1174045365675491328, 'asd', 'asd', NULL, NULL, 1166010721390034944, '2023-11-14 17:57:07.887',
        1166010721390034944, '2023-11-21 11:13:11.12', 1, 1, 0);
INSERT INTO "public"."sys_role" ("id", "name", "code", "des", "sort", "created_by", "created_time", "updated_by",
                                 "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1172486186615177216, '管理员', '32', '', 0, 1166010721390034944, '2023-11-10 10:41:30.653', 1166010721390034944,
        '2023-11-15 11:27:15.408', 0, 3, 0);
INSERT INTO "public"."sys_role" ("id", "name", "code", "des", "sort", "created_by", "created_time", "updated_by",
                                 "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1174045352702377984, 'asd', 'asd2121', NULL, NULL, 1166010721390034944, '2023-11-14 17:57:04.793',
        1166010721390034944, '2023-11-15 11:27:15.414', 0, 1, 0);
INSERT INTO "public"."sys_role" ("id", "name", "code", "des", "sort", "created_by", "created_time", "updated_by",
                                 "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1174045472282378240, 'asd', 'asd2121', NULL, NULL, 1166010721390034944, '2023-11-14 17:57:04.793',
        1166010721390034944, '2023-11-15 11:27:15.414', 0, 1, 0);
INSERT INTO "public"."sys_role" ("id", "name", "code", "des", "sort", "created_by", "created_time", "updated_by",
                                 "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1174045504948273152, 'asd', 'asd2121', NULL, NULL, 1166010721390034944, '2023-11-14 17:57:04.793',
        1166010721390034944, '2023-11-15 11:27:15.414', 0, 1, 0);
INSERT INTO "public"."sys_role" ("id", "name", "code", "des", "sort", "created_by", "created_time", "updated_by",
                                 "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1174317582145159168, '阿斯顿自行车自行车自行车行政村',
        '阿斯顿自行车自行车自行车行政村阿斯顿自行车自行车自行车行政村阿斯顿自行车自行车自行车行政村阿斯顿自行车自行车自行车行政村',
        '阿斯顿自行车自行车自行车行政村阿斯顿自行车自行车自行车行政村阿斯顿自行车自行车自行车行政村阿斯顿自行车自行车自行车行政村',
        NULL, 1166010721390034944, '2023-11-15 11:58:49.351', 1166010721390034944, '2023-11-21 11:13:11.103', 1, 0, 0);
INSERT INTO "public"."sys_role" ("id", "name", "code", "des", "sort", "created_by", "created_time", "updated_by",
                                 "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1174317517359808512, 'asd', 'zxc', NULL, NULL, 1166010721390034944, '2023-11-15 11:58:33.907',
        1166010721390034944, '2023-11-21 11:13:11.115', 1, 0, 0);
INSERT INTO "public"."sys_role" ("id", "name", "code", "des", "sort", "created_by", "created_time", "updated_by",
                                 "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1174045505502052352, 'asd', 'asd222323', NULL, NULL, 1166010721390034944, '2023-11-14 17:57:07.887',
        1166010721390034944, '2023-11-21 11:13:11.116', 1, 7, 0);
INSERT INTO "public"."sys_role" ("id", "name", "code", "des", "sort", "created_by", "created_time", "updated_by",
                                 "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1174306477028933632, 'asd', 'asd2', '123', NULL, 1166010721390034944, '2023-11-14 17:57:07.887',
        1166010721390034944, '2023-11-21 11:13:11.117', 1, 2, 0);
INSERT INTO "public"."sys_role" ("id", "name", "code", "des", "sort", "created_by", "created_time", "updated_by",
                                 "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1174045504411271168, 'asd', 'asd', NULL, NULL, 1166010721390034944, '2023-11-14 17:57:07.887',
        1166010721390034944, '2023-11-21 11:13:11.118', 1, 1, 0);
INSERT INTO "public"."sys_role" ("id", "name", "code", "des", "sort", "created_by", "created_time", "updated_by",
                                 "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1174045502603264000, 'asd', 'asd', NULL, NULL, 1166010721390034944, '2023-11-14 17:57:07.887',
        1166010721390034944, '2023-11-21 11:13:11.118', 1, 1, 0);
INSERT INTO "public"."sys_role" ("id", "name", "code", "des", "sort", "created_by", "created_time", "updated_by",
                                 "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1174045503853297664, 'asd', 'asd', NULL, NULL, 1166010721390034944, '2023-11-14 17:57:07.887',
        1166010721390034944, '2023-11-21 11:13:11.119', 1, 1, 0);
INSERT INTO "public"."sys_role" ("id", "name", "code", "des", "sort", "created_by", "created_time", "updated_by",
                                 "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1174045499977498624, 'asd', 'asd', NULL, NULL, 1166010721390034944, '2023-11-14 17:57:07.887',
        1166010721390034944, '2023-11-21 11:13:11.119', 1, 1, 0);
INSERT INTO "public"."sys_role" ("id", "name", "code", "des", "sort", "created_by", "created_time", "updated_by",
                                 "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1177970500958158848, 'wasad', 'asd', NULL, NULL, 1166010721390034944, '2023-11-25 13:54:13.064',
        1166010721390034944, '2023-11-25 13:54:13.065', 0, 0, 0);
INSERT INTO "public"."sys_role" ("id", "name", "code", "des", "sort", "created_by", "created_time", "updated_by",
                                 "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1177970880995786752, '21', '21', NULL, NULL, 1166010721390034944, '2023-11-25 13:55:43.671',
        1166010721390034944, '2023-11-25 13:55:43.672', 0, 0, 0);
INSERT INTO "public"."sys_role" ("id", "name", "code", "des", "sort", "created_by", "created_time", "updated_by",
                                 "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1172590168675450880, 'test', 'test', '', 1, 1166010721390034944, '2023-11-10 17:34:41.894', 1166010721390034944,
        '2023-11-25 13:55:46.907', 1, 3, 0);
INSERT INTO "public"."sys_role" ("id", "name", "code", "des", "sort", "created_by", "created_time", "updated_by",
                                 "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1172590168675451000, 'test', 'test', NULL, 1, 1166010721390034944, '2023-11-10 17:34:41.894',
        1166010721390034944, '2023-11-25 13:55:49.595', 1, 3, 0);
INSERT INTO "public"."sys_role" ("id", "name", "code", "des", "sort", "created_by", "created_time", "updated_by",
                                 "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1180573770486775808, '访客', 'visitor', NULL, NULL, 1166010721390034944, '2023-12-02 18:18:40.885',
        1166010721390034944, '2023-12-02 18:18:40.747', 0, 0, 0);
INSERT INTO "public"."sys_role" ("id", "name", "code", "des", "sort", "created_by", "created_time", "updated_by",
                                 "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1180629283373449216, '3232', '3232', NULL, NULL, 1166010721390034944, '2023-12-02 21:59:16.194',
        1166010721390034944, '2023-12-02 21:59:16.06', 0, 0, 0);
INSERT INTO "public"."sys_role" ("id", "name", "code", "des", "sort", "created_by", "created_time", "updated_by",
                                 "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1180629299806863360, '434', '34', NULL, NULL, 1166010721390034944, '2023-12-02 21:59:20.111',
        1166010721390034944, '2023-12-02 21:59:19.989', 0, 0, 0);
INSERT INTO "public"."sys_role" ("id", "name", "code", "des", "sort", "created_by", "created_time", "updated_by",
                                 "updated_time", "is_deleted", "version", "tenant_id")
VALUES (1180629314222817280, '54', '54', NULL, NULL, 1166010721390034944, '2023-12-02 21:59:23.55', 1166010721390034944,
        '2023-12-02 21:59:23.41', 0, 0, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role_menu";
CREATE TABLE "public"."sys_role_menu"
(
    "id"      int8 NOT NULL,
    "role_id" int8,
    "menu_id" int8
)
;
ALTER TABLE "public"."sys_role_menu" OWNER TO "postgres";
COMMENT
ON COLUMN "public"."sys_role_menu"."id" IS '主键';
COMMENT
ON COLUMN "public"."sys_role_menu"."role_id" IS '角色ID';
COMMENT
ON COLUMN "public"."sys_role_menu"."menu_id" IS '菜单ID';
COMMENT
ON TABLE "public"."sys_role_menu" IS '角色菜单关联表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO "public"."sys_role_menu" ("id", "role_id", "menu_id")
VALUES (1179125475407167488, 1172486186615177216, 1176525950863802368);
INSERT INTO "public"."sys_role_menu" ("id", "role_id", "menu_id")
VALUES (1179125475432464384, 1172486186615177216, 1175141121530527744);
INSERT INTO "public"."sys_role_menu" ("id", "role_id", "menu_id")
VALUES (1179125475432595456, 1172486186615177216, 1175140969742729216);
INSERT INTO "public"."sys_role_menu" ("id", "role_id", "menu_id")
VALUES (1179125475432726528, 1172486186615177216, 1176519513181650944);
INSERT INTO "public"."sys_role_menu" ("id", "role_id", "menu_id")
VALUES (1179125475437051904, 1172486186615177216, 1172187370985095168);
INSERT INTO "public"."sys_role_menu" ("id", "role_id", "menu_id")
VALUES (1179125475437182976, 1172486186615177216, 1172187336839135232);
INSERT INTO "public"."sys_role_menu" ("id", "role_id", "menu_id")
VALUES (1179125475437314048, 1172486186615177216, 1175513370862813184);
INSERT INTO "public"."sys_role_menu" ("id", "role_id", "menu_id")
VALUES (1179125475437445120, 1172486186615177216, 1172187214407270400);
INSERT INTO "public"."sys_role_menu" ("id", "role_id", "menu_id")
VALUES (1179125475437576192, 1172486186615177216, 1176547780762533888);
INSERT INTO "public"."sys_role_menu" ("id", "role_id", "menu_id")
VALUES (1179125475437707264, 1172486186615177216, 1177302820223713280);
INSERT INTO "public"."sys_role_menu" ("id", "role_id", "menu_id")
VALUES (1179125475437838336, 1172486186615177216, 1177303142178619392);
INSERT INTO "public"."sys_role_menu" ("id", "role_id", "menu_id")
VALUES (1179125475437969408, 1172486186615177216, 1179124112140795904);
INSERT INTO "public"."sys_role_menu" ("id", "role_id", "menu_id")
VALUES (1180592217401327616, 1180573770486775808, 1177302820223713280);
COMMIT;

-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_tenant";
CREATE TABLE "public"."sys_tenant"
(
    "id"           int8 NOT NULL,
    "tenant_name"  varchar(255) COLLATE "pg_catalog"."default",
    "created_by"   int8,
    "created_time" timestamp(6),
    "updated_by"   int8,
    "updated_time" timestamp(6),
    "is_deleted"   int2,
    "version"      int4 DEFAULT 0
)
;
ALTER TABLE "public"."sys_tenant" OWNER TO "postgres";
COMMENT
ON COLUMN "public"."sys_tenant"."id" IS '主键';
COMMENT
ON COLUMN "public"."sys_tenant"."tenant_name" IS '租户名称';
COMMENT
ON COLUMN "public"."sys_tenant"."created_by" IS '创建人';
COMMENT
ON COLUMN "public"."sys_tenant"."created_time" IS '创建时间';
COMMENT
ON COLUMN "public"."sys_tenant"."updated_by" IS '更新人';
COMMENT
ON COLUMN "public"."sys_tenant"."updated_time" IS '更新时间';
COMMENT
ON COLUMN "public"."sys_tenant"."is_deleted" IS '逻辑删除';
COMMENT
ON COLUMN "public"."sys_tenant"."version" IS '版本号';
COMMENT
ON TABLE "public"."sys_tenant" IS '系统租户';

-- ----------------------------
-- Records of sys_tenant
-- ----------------------------
BEGIN;
INSERT INTO "public"."sys_tenant" ("id", "tenant_name", "created_by", "created_time", "updated_by", "updated_time",
                                   "is_deleted", "version")
VALUES (0, 'Turbo门店', NULL, '2023-12-01 09:25:43.689', 1166010721390034944, '2023-11-30 09:25:48.326', NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user";
CREATE TABLE "public"."sys_user"
(
    "id"           int8 NOT NULL,
    "created_by"   int8,
    "created_time" timestamp(6),
    "updated_by"   int8,
    "updated_time" timestamp(6),
    "is_deleted"   int2,
    "version"      int4                                       DEFAULT 0,
    "tenant_id"    int8,
    "username"     varchar(64) COLLATE "pg_catalog"."default",
    "password"     varchar(64) COLLATE "pg_catalog"."default",
    "email"        varchar(64) COLLATE "pg_catalog"."default",
    "phone"        varchar(24) COLLATE "pg_catalog"."default",
    "status"       varchar(16) COLLATE "pg_catalog"."default" DEFAULT 'ENABLE':: character varying,
    "avatar"       varchar(255) COLLATE "pg_catalog"."default",
    "nickname"     varchar(64) COLLATE "pg_catalog"."default",
    "org_id"       int8
)
;
ALTER TABLE "public"."sys_user" OWNER TO "postgres";
COMMENT
ON COLUMN "public"."sys_user"."id" IS '主键';
COMMENT
ON COLUMN "public"."sys_user"."created_by" IS '创建人';
COMMENT
ON COLUMN "public"."sys_user"."created_time" IS '创建时间';
COMMENT
ON COLUMN "public"."sys_user"."updated_by" IS '更新人';
COMMENT
ON COLUMN "public"."sys_user"."updated_time" IS '更新时间';
COMMENT
ON COLUMN "public"."sys_user"."is_deleted" IS '逻辑删除';
COMMENT
ON COLUMN "public"."sys_user"."version" IS '版本号';
COMMENT
ON COLUMN "public"."sys_user"."tenant_id" IS '租户号';
COMMENT
ON COLUMN "public"."sys_user"."username" IS '用户名';
COMMENT
ON COLUMN "public"."sys_user"."password" IS '密码';
COMMENT
ON COLUMN "public"."sys_user"."email" IS '邮箱';
COMMENT
ON COLUMN "public"."sys_user"."phone" IS '电话号码';
COMMENT
ON COLUMN "public"."sys_user"."status" IS '状态 ENABLE 启用、禁用 DISABLED';
COMMENT
ON COLUMN "public"."sys_user"."avatar" IS '头像';
COMMENT
ON COLUMN "public"."sys_user"."nickname" IS '昵称';
COMMENT
ON COLUMN "public"."sys_user"."org_id" IS '组织id';
COMMENT
ON TABLE "public"."sys_user" IS '系统用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO "public"."sys_user" ("id", "created_by", "created_time", "updated_by", "updated_time", "is_deleted",
                                 "version", "tenant_id", "username", "password", "email", "phone", "status", "avatar",
                                 "nickname", "org_id")
VALUES (1172583529205202944, 1166010721390034944, '2023-11-10 17:08:18.914', 1166010721390034944,
        '2023-11-10 17:16:57.812', 0, 1, 0, 'jiangwei', 'jiangwei', 'jiangw1027@gmail.com', NULL, 'ENABLE', NULL, NULL,
        1180175387064532992);
INSERT INTO "public"."sys_user" ("id", "created_by", "created_time", "updated_by", "updated_time", "is_deleted",
                                 "version", "tenant_id", "username", "password", "email", "phone", "status", "avatar",
                                 "nickname", "org_id")
VALUES (1180483526638698496, 1166010721390034944, '2023-12-02 12:20:05.075', 1180483526638698496,
        '2023-12-02 13:11:33.497', 0, 0, 0, 'jw', 'S517481nHr8Tm0Bxf4pVMg==', NULL, NULL, 'ENABLE',
        'http://127.0.0.1:8600/sys/attachment/download/113543189_p0_master1200.jpg', NULL, NULL);
INSERT INTO "public"."sys_user" ("id", "created_by", "created_time", "updated_by", "updated_time", "is_deleted",
                                 "version", "tenant_id", "username", "password", "email", "phone", "status", "avatar",
                                 "nickname", "org_id")
VALUES (1166010721390034944, NULL, '2023-10-23 13:50:20.905', 1166010721390034944, '2023-12-02 19:43:40.098', 0, NULL,
        0, 'admin', 'feESonRcIsnjgXaFxBV4AA==', 'jiangw1027@gmail.com', '12311313', 'ENABLE',
        'http://127.0.0.1:8600/sys/attachment/download/113543189_p0_master1200.jpg', 'jjjjjjjj', 1179129791754403840);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_post";
CREATE TABLE "public"."sys_user_post"
(
    "id"      int8 NOT NULL,
    "user_id" int8,
    "post_id" int8
)
;
ALTER TABLE "public"."sys_user_post" OWNER TO "postgres";
COMMENT
ON COLUMN "public"."sys_user_post"."id" IS '主键';
COMMENT
ON COLUMN "public"."sys_user_post"."user_id" IS '用户ID';
COMMENT
ON COLUMN "public"."sys_user_post"."post_id" IS '组织ID';
COMMENT
ON TABLE "public"."sys_user_post" IS '用户组织';

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
BEGIN;
INSERT INTO "public"."sys_user_post" ("id", "user_id", "post_id")
VALUES (1180174583036772352, 1166010721390034944, 1180165158766379008);
INSERT INTO "public"."sys_user_post" ("id", "user_id", "post_id")
VALUES (1180174709738438656, 1172583529205202944, 1180165158766379008);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_role";
CREATE TABLE "public"."sys_user_role"
(
    "id"      int8 NOT NULL,
    "user_id" int8,
    "role_id" int8
)
;
ALTER TABLE "public"."sys_user_role" OWNER TO "postgres";
COMMENT
ON COLUMN "public"."sys_user_role"."id" IS '主键';
COMMENT
ON COLUMN "public"."sys_user_role"."user_id" IS '用户id';
COMMENT
ON COLUMN "public"."sys_user_role"."role_id" IS '角色id';
COMMENT
ON TABLE "public"."sys_user_role" IS '用户角色关联';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO "public"."sys_user_role" ("id", "user_id", "role_id")
VALUES (1180171866552795136, 1166010721390034944, 1172486186615177216);
INSERT INTO "public"."sys_user_role" ("id", "user_id", "role_id")
VALUES (1180171866557120512, 1166010721390034944, 1174045352702377984);
INSERT INTO "public"."sys_user_role" ("id", "user_id", "role_id")
VALUES (1180171866557251584, 1166010721390034944, 1174045472282378240);
INSERT INTO "public"."sys_user_role" ("id", "user_id", "role_id")
VALUES (1180171866557382656, 1166010721390034944, 1174045504948273152);
INSERT INTO "public"."sys_user_role" ("id", "user_id", "role_id")
VALUES (1180171866561708032, 1166010721390034944, 1177970500958158848);
INSERT INTO "public"."sys_user_role" ("id", "user_id", "role_id")
VALUES (1180171866561839104, 1166010721390034944, 1177970880995786752);
INSERT INTO "public"."sys_user_role" ("id", "user_id", "role_id")
VALUES (1180593403392557056, 1180483526638698496, 1180573770486775808);
COMMIT;

-- ----------------------------
-- Primary Key structure for table sys_attachment
-- ----------------------------
ALTER TABLE "public"."sys_attachment"
    ADD CONSTRAINT "sys_attachments_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_cloud_storage_config
-- ----------------------------
ALTER TABLE "public"."sys_cloud_storage_config"
    ADD CONSTRAINT "sys_cloud_storage_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_dic
-- ----------------------------
ALTER TABLE "public"."sys_dic"
    ADD CONSTRAINT "sys_dic_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_menu
-- ----------------------------
ALTER TABLE "public"."sys_menu"
    ADD CONSTRAINT "sys_menu_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_org
-- ----------------------------
ALTER TABLE "public"."sys_org"
    ADD CONSTRAINT "sys_org_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_post
-- ----------------------------
ALTER TABLE "public"."sys_post"
    ADD CONSTRAINT "sys_post_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_role
-- ----------------------------
ALTER TABLE "public"."sys_role"
    ADD CONSTRAINT "sys_role_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_role_menu
-- ----------------------------
ALTER TABLE "public"."sys_role_menu"
    ADD CONSTRAINT "sys_role_menu_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_tenant
-- ----------------------------
ALTER TABLE "public"."sys_tenant"
    ADD CONSTRAINT "sys_tenant_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_user
-- ----------------------------
ALTER TABLE "public"."sys_user"
    ADD CONSTRAINT "sys_user_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_user_post
-- ----------------------------
ALTER TABLE "public"."sys_user_post"
    ADD CONSTRAINT "sys_user_org_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_user_role
-- ----------------------------
ALTER TABLE "public"."sys_user_role"
    ADD CONSTRAINT "sys_user_role_pkey" PRIMARY KEY ("id");
