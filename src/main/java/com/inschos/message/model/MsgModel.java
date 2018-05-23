package com.inschos.message.model;
//消息 模板表
public class MsgModel {

    public long id;

    public String model_code;//'模板代码'

    public String model_name;//'模板名称'

    public String model_content;//'模板详细内容'

    public int model_type;//'模板类型'

    public long created_user;//'创建用户id'

    public int created_user_type;//'创建用户type'

    public int status;//'审核状态:默认为0审核中，1审核通过，2审核失败'

    public int state;//'删除标识:默认为0，1未删除'

    public long created_at;//'创建时间，毫秒'

    public long updated_at;//'更新时间，毫秒'

}
