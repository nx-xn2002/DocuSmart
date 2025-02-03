# 数据库初始化

-- 创建库
create database if not exists docu_smart;

-- 切换库
use docu_smart;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    account      varchar(256)                           not null comment '账号',
    password     varchar(512)                           not null comment '密码',
    username     varchar(256)                           null comment '用户昵称',
    access_key   varchar(512)                           not null comment 'accessKey',
    secret_key   varchar(512)                           not null comment 'secretKey',
    user_avatar  varchar(1024)                          null comment '用户头像',
    user_profile varchar(512)                           null comment '用户简介',
    user_role    varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    create_time  datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted   tinyint      default 0                 not null comment '是否删除'
) comment '用户';
