package com.inschos.message.data.mapper;

import com.inschos.message.model.*;

import java.util.List;

public interface MsgModelMapper {
    //添加模板
    int addMsgModel(MsgModel msgModel);//定义返回数据类型-整形-影响数据库行数

    //获取消息 模板列表
    MsgModel findMsgModelRepeat(MsgModel msgModel);//定义返回数据类型-对象

    //获取消息 模板列表
    List<MsgModel> findMsgModelList(MsgModelList msgModelList);//定义返回数据类型-对象

    //获取消息 模板详情
    MsgModel findMsgModelInfo(MsgModel msgModel);//定义返回数据类型-对象

    //更新消息 模板
    int updateMsgModel(MsgModelUpdate msgModelUpdate);//定义返回数据类型-整形-影响数据库行数
    //更新消息 模板-状态
    int updateMsgModelStatus(MsgModelUpdate msgModelUpdate);//定义返回数据类型-整形-影响数据库行数
    //更新消息 模板-类型
    int updateMsgModelType(MsgModelUpdate msgModelUpdate);//定义返回数据类型-整形-影响数据库行数
}
