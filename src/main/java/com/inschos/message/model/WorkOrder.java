package com.inschos.message.model;

/**
 * Created by IceAnt on 2018/5/21.
 */
public class WorkOrder {

    /** */
    public long id;

    /** 工单标题*/
    public String title;

    /** 工单内容*/
    public String content;

    /** 分类id*/
    public long category_id;

    /** 收件人 UUID*/
    public String addressee_uuid;

    /** 发件人 UUID*/
    public String sender_uuid;

    /** 工单类型  1 对业管的  2 业管对天眼的*/
    public int type;

    /** 结单状态:1未关闭 2已关闭*/
    public int close_status;

    /** 处理结果:1无反馈 2未解决 3已解决*/
    public int solve_status;

    /** 处理状态:1待处理 2处理中 3已处理*/
    public int handle_status;

    /** 创建时间*/
    public long created_at;

    /** 修改时间*/
    public long updated_at;

    /** 删除标识 0删除 1未删除*/
    public int state;


}
