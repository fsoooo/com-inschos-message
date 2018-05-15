package com.inschos.message.data.dao;

import com.inschos.message.data.mapper.MsgInboxMapper;
import com.inschos.message.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 站内信收件处理数据访问对象（按功能划分DAO）
 * 收件箱列表(findMsgRecList)，发件箱列表(findMsgSysList),用户未接收信息查询(findMsgSysALL),站内信详情（findMsgInfo），删除/读取站内信（updateMsgRec）
 * TODO  在DAO里传对象，需要判空！！！
 */
@Component
public class MsgInboxDAO {
    @Autowired
    private MsgInboxMapper msgInboxMapper;

    /**
     * 收件箱列表查询
     *
     * @param user_id|用户ID(收件人)
     * @param user_type|用户类型(收件人)
     * @param message_status|站内信状态:未读 0/已读 1 （非必传，默认为0）
     * @param page|分页页码               （非必传，默认为1）
     * @param limit|每页显示行数            （非必传，默认为10）
     * @return mixed
     * @access public
     */
    public List<MsgInbox> findMsgRecList(MsgRec msgRec) {
        return msgInboxMapper.findMsgRecList(msgRec);
    }

    /**
     * 收取站内信（系统把站内信同步到用户收件箱,同时修改系统发件表的状态）
     *
     * @param msg_id|消息标识列
     * @param user_id|用户ID(收件人)
     * @param user_type|发件人类型，个人用户1/企业用户2/业管用户等3
     * @param sys_status|消息状态：默认为未读0/已读1
     * @return mixed
     * @access public
     */
    public int insertMsgRec(MsgRec msgRec) {
        return msgInboxMapper.insertMsgRec(msgRec);
    }

    /**
     * 收取站内信（系统把站内信同步到用户收件箱,同时修改系统发件表的状态）
     *
     * @param id|消息标识列
     * @param status|读取状态
     * @return mixed
     * @access public
     */
    public int updateMsgSysStatus(MsgSys msgSys) {
        return msgInboxMapper.updateMsgSysStatus(msgSys);
    }

    /**
     * 用户未收件(用户登录之后，查询系统收件箱，用户为读取的消息)
     *
     * @param user_id|用户ID(收件人)
     * @param user_type|发件人类型，个人用户1/企业用户2/业管用户等
     * @return mixed
     * @access public
     */
    public List<MsgSys> findUserMsgRes(MsgRec msgRec) {
        return msgInboxMapper.findUserMsgRes(msgRec);
    }

    /**
     * 发件箱列表查询
     *
     * @param user_id|用户ID(发件人)
     * @param user_type|用户类型(发件人)
     * @param page|分页页码               （非必传，默认为1）
     * @param limit|每页显示行数            （非必传，默认为10）
     * @param message_status|站内信状态:未读 0/已读 1 （非必传，默认为0）
     * @return mixed
     * @access public
     */
    public List<MsgSys> findMsgSysList(MsgSys msgSys) {
        return msgInboxMapper.findMsgSysList(msgSys);
    }

    /**
     * 发件箱列表查询
     *
     * @param msg_id
     * @return mixed
     * @access public
     */
    public MsgSys findMsgSysInfo(MsgSys msgSys){
        return msgInboxMapper.findMsgSysInfo(msgSys);
    }

    /**
     * 站内信详情查询
     *
     * @param id|站内信id
     * @return mixed
     * @access public
     */
    public MsgRec findMsgInfo(MsgRec msgRec) {
        return msgInboxMapper.findMsgInfo(msgRec);
    }

    /**
     * 删除/读取站内信（update）
     *
     * @param msg_id|站内信id
     * @param update_data|更新数据 ['deleted_at'=>time()] 删除
     *                         ['sys_status'=>'1'] 标记已读
     * @return mixed
     * @access public
     */
    public int updateMsgRecStatus(MsgUpdate msgUpdate) {
        return msgInboxMapper.updateMsgRecStatus(msgUpdate);
    }

    /**
     * 删除/读取站内信（update）
     *
     * @param msg_id|站内信id
     * @param update_data|更新数据 ['deleted_at'=>time()] 删除
     *                         ['sys_status'=>'1'] 标记已读
     * @return mixed
     * @access public
     */
    public int updateMsgRecState(MsgUpdate msgUpdate) {
        return msgInboxMapper.updateMsgRecState(msgUpdate);
    }


}
