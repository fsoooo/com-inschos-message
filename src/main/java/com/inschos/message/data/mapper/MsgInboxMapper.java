package com.inschos.message.data.mapper;

import com.inschos.message.model.*;

import java.util.List;

//todo 注意,这个是接口-interface
//Mapper接口开发需要遵循以下规范：
//1、Mapper.xml文件中的namespace与mapper接口的类路径相同。
//2、Mapper接口方法名和Mapper.xml中定义的每个statement的id相同
//3、Mapper接口方法的输入参数类型和mapper.xml中定义的每个sql 的parameterType的类型相同
//4、Mapper接口方法的输出参数类型和mapper.xml中定义的每个sql的resultType的类型相同
public interface MsgInboxMapper {
    // 收件箱列表(findMsgRecList)MsgTypeLists
    List<MsgTypeLists> findMsgRecList(MsgRec msgRec);

    //某一分类的收件箱的列表(findMsgRecListByType)MsgRec
    List<MsgRec> findMsgRecListByType(MsgRec msgRec);

    //用户未收件(用户登录之后，查询系统收件箱，用户为读取的消息)
    List<MsgSys> findUserMsgRes(MsgRec msgRec);

    //从消息发送对象表获取未读消息
    List<MsgSys> findMsgToRecord(MsgRec msgRec);

    //获取消息主体表信息
    MsgSys findMsgSysRes(MsgSys msgSys);

    //收取消息 （系统把消息 同步到用户收件箱,同时修改系统发件表的状态）
    int insertMsgRec(MsgRec msgRec);

    MsgCount findMsgRecCount(MsgRec msgRec);

    //更改系统消息读取状态
    int updateMsgSysStatus(MsgSys msgSys);

    MsgCount findMsgSysCount(MsgSys msgSys);

    // 发件箱列表(findMsgSysList)
    List<MsgTypeLists> findMsgSysList(MsgSys msgSys);

    //某一分类的收件箱的列表(findMsgSysListByType)MsgRec
    List<MsgSys> findMsgSysListByType(MsgSys msgSys);

    //发件箱详情
    MsgSys findMsgSysInfo(MsgSys msgSys);

    // 消息 详情(findMsgInfo)
    MsgRec findMsgInfo(MsgRec msgRec);

    List<MsgTo> findMsgTo(MsgSys msgSys);

    // 删除/读取消息 (updateMsgRec)
    int updateMsgRecStatus(MsgUpdate msgUpdate);

    // 删除/读取消息 (updateMsgRec)
    int updateMsgRecState(MsgUpdate msgUpdate);

    // 删除/读取消息 (updateMsgRec)
    int updateAllMsgRecStatus(MsgUpdate msgUpdate);

    // 删除/读取消息 (updateMsgRec)
    int updateAllMsgRecState(MsgUpdate msgUpdate);

}