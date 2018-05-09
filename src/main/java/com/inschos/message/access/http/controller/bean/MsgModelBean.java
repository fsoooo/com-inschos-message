package com.inschos.message.access.http.controller.bean;

import com.inschos.message.model.Page;

public class MsgModelBean {
    //内部类-静态方法
    //添加站内信模板
    public static class msgModelAdd extends BaseRequest {

        public String model_code;//'模板代码'

        public String model_name;//'模板名称'

        public String model_content;//'模板详细内容'

        public long created_user;//'创建用户id'

        public int created_user_type;//'创建用户type'

        public int status = 0;//'审核状态:默认为0审核中，1审核通过，2审核失败'

        public int state = 0;//'删除标识:默认为0，1未删除'

        public long created_at;//'创建时间，毫秒'

        public long updated_at;//'更新时间，毫秒'
    }

    //更新站内信状态
    public static class MsgModelUpdate extends BaseRequest {

        public String model_code;//'模板代码'

        public int status;//'审核状态:默认为0审核中，1审核通过，2审核失败'
    }

    //站内信模板列表
    public static class MsgModelList extends BaseRequest {

        public Page page;//分页

        public int model_status = 0;//模板状态（审核通过0/未通过1/已删除2）
    }

    //获取站内信模板详情
    public static class MsgModelInfo extends BaseRequest{

        public long model_code;
    }

}
