package com.inschos.message.model;

/**
 * Created by IceAnt on 2018/5/21.
 */
public class WorkOrderReply {

    /** */
    public long id;

    /** 工单id*/
    public long work_order_id;

    /** 回复人UUID*/
    public String replier_uuid;

    /** 给指定的已回复的记录回复*/
    public long to_reply_id;

    /** 回复内容*/
    public String content;

    /** 创建时间*/
    public long created_at;

}
