package com.inschos.message.model;

/**
 * Created by IceAnt on 2018/5/21.
 */
public class WorkOrderCategory {

    public final static int WRITABLE_OK = 1;

    /** */
    public long id;

    /** 分类名*/
    public String name;

    /** 0不可写 1可写入*/
    public int writable;

    /** 创建时间*/
    public long created_at;

}
