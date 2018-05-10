package com.inschos.message.data.mapper;

import com.inschos.message.model.*;

public interface MsgModelMapper {
    //添加模板
    int addMsgModel(MsgModel msgModel);//定义返回数据类型-整形-影响数据库行数

    //获取站内信模板列表
    MsgModel getMsgModelRepeat(MsgModel msgModel);//定义返回数据类型-对象

    //获取站内信模板列表
    MsgModel getMsgModelList(MsgModelList msgModelList);//定义返回数据类型-对象

    //获取站内信模板详情
    MsgModel getMsgModelInfo(String model_code);//定义返回数据类型-对象

    //更新站内信模板状态
    int updateMsgModel(MsgModelUpdate msgModelUpdate);//定义返回数据类型-整形-影响数据库行数
}
