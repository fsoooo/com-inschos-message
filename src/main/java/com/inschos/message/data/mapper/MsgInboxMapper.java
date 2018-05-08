package com.inschos.message.data.mapper;

import com.inschos.message.model.*;

//todo 注意,这个是接口-interface
//Mapper接口开发需要遵循以下规范：
//1、Mapper.xml文件中的namespace与mapper接口的类路径相同。
//2、Mapper接口方法名和Mapper.xml中定义的每个statement的id相同
//3、Mapper接口方法的输入参数类型和mapper.xml中定义的每个sql 的parameterType的类型相同
//4、Mapper接口方法的输出参数类型和mapper.xml中定义的每个sql的resultType的类型相同
public interface MsgInboxMapper {
    // 收件箱列表(getMsgRecList)MsgRec
    MsgRec getMsgRecList(MsgRec msgRec);//定义返回数据类型-对象

    // 发件箱列表(getMsgSysList)
    MsgSys getMsgSysList(MsgSys msgSys);//定义返回数据类型-对象

    // 用户未接收信息查询(getMsgSysALL),
    MsgSys getMsgSysALL(MsgSys msgSys);//定义返回数据类型-对象

    // 站内信详情(getMsgInfo)
    MsgInbox getMsgInfo(long msg_id);//定义返回数据类型-对象

    // 删除/读取站内信(updateMsgRec)
    int updateMsgRec(MsgUpdate msgUpdate);//定义返回数据类型-整形-影响数据库行数

    //收取站内信（系统把站内信同步到用户收件箱,同时修改系统发件表的状态）
    int insertMsgRec(MsgRec msgRec);//定义返回数据类型-整形-影响数据库行数
}