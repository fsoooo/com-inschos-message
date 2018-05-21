package com.inschos.message.access.http.controller.bean;

public class WorkOrderBean {

   public static class addWork extends BaseRequest{

    public String title; //工单标题

    public String content; //工单内容

    public int category_id; //工单分类

    public String addressee_uuid; //收件人uuid

    public String sender_uuid; //发件人 uuid

    public int type; //工单类型  1 对业管的  2 业管对天眼的

    public int close_status = 1;//结单状态:1未关闭 2已关闭

    public int solve_status;//处理结果:1无反馈 2未解决 3已解决

    public int handle_status = 1;//处理状态:1待处理 2处理中 3已处理

    public int state = 0;//删除标识 0删除 1未删除

   }

}
