package com.inschos.message.access.http.controller.bean;

//TODO 内部类-静态方法
public class MsgModelBean {

    //添加站内信模板
    public static class msgModelAdd extends BaseRequest {

        public String model_code;//'模板代码'

        public String model_name;//'模板名称'

        public String model_content;//'模板详细内容'

        public long created_user;//'创建用户id'

        public int created_user_type;//'创建用户type'

        public int status = 0;//'审核状态:默认为0审核中，1审核通过，2审核失败'

        public int state = 0;//'删除标识:默认为0，1未删除'

    }


    //站内信模板列表
    public static class msgModelList extends BaseRequest {

        public PageBean pageBean;//分页数据

        public int model_status = 0;//模板状态（审核通过0/未通过1/已删除2）
    }

    //获取站内信模板详情
    public static class msgModelInfo extends BaseRequest {

        public String model_code;//模板代码
    }

    //更新站内信状态
    public static class msgModelUpdate extends BaseRequest {

        public String model_code;//'模板代码'

        public int status;//'审核状态:默认为0审核中，1审核通过，2审核失败'

        public long user_id;//操作人id

        public int user_type;//操作人类型（只有业管可以审核和删除）
    }

}
