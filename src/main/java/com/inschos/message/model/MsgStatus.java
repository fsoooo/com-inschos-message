package com.inschos.message.model;

public class MsgStatus {

    /**
     * 消息列表状态(未读/已读/全部/删除)
     */
    public final static int MSG_READ = 1;
    public final static int MSG_UNREAD = 2;
    public final static int MSG_All = 3;
    public final static int MSG_DEL = 4;

    public static String getListStatus(int listStatus) {
        String result;
        switch (listStatus) {
            case 1:
                result = "未读";
                break;
            case 2:
                result = "已读";
                break;
            case 3:
                result = "全部";
                break;
            case 4:
                result = "删除";
                break;
            default:
                result = "--";
                break;
        }
        return result;
    }

    /**
     * 用户类型(个人/企业/代理人/业管)
     */
    public final static int USER_PERSON = 1;
    public final static int USER_COMPANY = 2;
    public final static int USER_AGENT = 3;
    public final static int USER_MANAGER = 4;

    public static String getUserType(int userType) {
        String result;
        switch (userType) {
            case 1:
                result = "个人用户";
                break;
            case 2:
                result = "企业用户";
                break;
            case 3:
                result = "代理人用户";
                break;
            case 4:
                result = "业管用户";
                break;
            default:
                result = "--";
                break;
        }
        return result;
    }

    /**
     * 消息类型(系统消息/保单助手/理赔进度/最新任务/客户消息/活动提示/顾问消息)
     */
    public final static int MSG_SYSTEM = 1;
    public final static int MSG_WARRANTY = 2;
    public final static int MSG_CLAIM = 3;
    public final static int MSG_TASK = 4;
    public final static int USER_CUSTOMER = 5;
    public final static int USER_ACTIVITY = 6;
    public final static int USER_ADVISER = 7;

    public static String getMsgType(int msgType) {
        String result;
        switch (msgType) {
            case 1:
                result = "系统消息";
                break;
            case 2:
                result = "保单助手";
                break;
            case 3:
                result = "理赔进度";
                break;
            case 4:
                result = "最新任务";
                break;
            case 5:
                result = "客户消息";
                break;
            case 6:
                result = "活动提示";
                break;
            case 7:
                result = "顾问消息";
                break;
            default:
                result = "--";
                break;
        }
        return result;
    }

    /**
     * 系统消息，收件人id，type值
     */
    public final static int MSG_SYS_KEY = -1;

}
