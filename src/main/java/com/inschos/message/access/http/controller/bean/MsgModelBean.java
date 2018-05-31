package com.inschos.message.access.http.controller.bean;

import com.inschos.message.annotation.CheckParams;

//TODO 内部类-静态方法
public class MsgModelBean {

    //添加消息 模板
    public static class AddRequest extends BaseRequest {

        @CheckParams(stringType = CheckParams.StringType.STRING, maxLen = 10, minLen = 2)
        public String modelName;//'模板名称'

        @CheckParams(stringType = CheckParams.StringType.STRING, minLen = 2)
        public String modelContent;//'模板详细内容'

        @CheckParams(stringType = CheckParams.StringType.STRING, minLen = 1)
        public int modelType;//'模板类型'
    }

    //消息 模板列表
    public static class ListRequest extends BaseRequest {

        public String pageNum;//分页数据

        public String lastId;//分页数据

        public String limit;//分页数据

        public int modelStatus = 0;//模板状态（审核通过0/未通过1/已删除2）

        @CheckParams(stringType = CheckParams.StringType.STRING, minLen = 1)
        public int modelType;//'模板类型'
    }

    //获取消息 模板详情
    public static class InfoRequest extends BaseRequest {

        @CheckParams(stringType = CheckParams.StringType.STRING, minLen = 1)
        public String modelCode;//模板代码
    }

    //更新消息
    public static class UpdateRequest extends BaseRequest {

        @CheckParams(stringType = CheckParams.StringType.STRING, minLen = 1)
        public String modelCode;//'模板代码'

        @CheckParams(stringType = CheckParams.StringType.NUMBER, minLen = 1)
        public int status;//'审核状态:默认为1审核中，2审核通过，3审核失败'

        @CheckParams(stringType = CheckParams.StringType.STRING, minLen = 1)
        public int modelType;//'模板类型'

        @CheckParams(stringType = CheckParams.StringType.NUMBER, minLen = 1)
        public long userId;//操作人id

        @CheckParams(stringType = CheckParams.StringType.NUMBER, minLen = 1)
        public int userType;//操作人类型（只有业管可以审核和删除）
    }

}
