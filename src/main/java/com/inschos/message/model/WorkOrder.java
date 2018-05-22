package com.inschos.message.model;

/**
 * Created by IceAnt on 2018/5/21.
 */
public class WorkOrder {


    public final static int TYPE_MANAGER = 1;

    public final static int TYPE_INS = 2;

    public final static int STATUS_CLOSED = 2;

    public final static int STATUS_CLOSE_NO = 1;

    public final static int STATUS_HANDLE_WAITING = 1;
    public final static int STATUS_HANDLE_ING = 2;
    public final static int STATUS_HANDLE_DONE = 3;

    public final static int STATUS_SOLVE_WEIFANKUI = 1;

    public final static int STATUS_SOLVE_NO = 2;

    public final static int STATUS_SOLVE_OK = 3;

    /** */
    public long id;

    public String wo_num;

    /** 工单标题*/
    public String title;

    /** 工单内容*/
    public String content;

    /** 分类id*/
    public long category_id;

    /** 自定义分类名 */
    public String category_extra_name;

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

    public Page page;

    public static String  getResult(int solveStatus){
        String result ;
        switch (solveStatus){
            case 2:
                result = "未解决";
                break;
            case 3:
                result = "已解决";
                break;
            default:
                result = "--";
                break;
        }
        return result;
    }

    public static String  getHandle(int handleStatus){
        String result ;
        switch (handleStatus){
            case 1:
                result = "待处理";
                break;
            case 2:
                result = "处理中";
                break;
            case 3:
                result = "已处理";
                break;
            default:
                result = "--";
                break;
        }
        return result;
    }


}
