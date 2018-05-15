package com.inschos.message.data.dao;

import com.inschos.message.data.mapper.MsgInboxMapper;
import com.inschos.message.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 站内信收件处理数据访问对象（按功能划分DAO）
 * 收件箱列表(getMsgRecList)，发件箱列表(getMsgSysList),用户未接收信息查询(getMsgSysALL),站内信详情（getMsgInfo），删除/读取站内信（updateMsgRec）
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
    public List<MsgInbox> getMsgRecList(MsgRec msgRec) {
        return msgInboxMapper.getMsgRecList(msgRec);
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
     * 用户未收件(用户登录之后，查询系统收件箱，用户为读取的消息)
     *
     * @param user_id|用户ID(收件人)
     * @param user_type|发件人类型，个人用户1/企业用户2/业管用户等
     * @return mixed
     * @access public
     */
    public List<MsgRec> getUserMsgRes(MsgRec msgRec){
        return msgInboxMapper.getUserMsgRes(msgRec);
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
    public List<MsgSys> getMsgSysList(MsgSys msgSys) {
        return msgInboxMapper.getMsgSysList(msgSys);
    }

    /**
     * 站内信详情查询
     *
     * @param msg_id|站内信id
     * @return mixed
     * @access public
     */
    public MsgInbox getMsgInfo(long msg_id) {
        return msgInboxMapper.getMsgInfo(msg_id);
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
    public int updateMsgRec(MsgUpdate msgUpdate) {
        return msgInboxMapper.updateMsgRec(msgUpdate);
    }

}
