package com.inschos.message.data.dao;

import com.inschos.message.data.mapper.MsgInboxMapper;
import com.inschos.message.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
     * @access public
     * @param user_id|用户ID(收件人)
     * @param user_type|用户类型(收件人)
     * @param message_status|站内信状态:未读 0/已读 1 （非必传，默认为0）
     * @param page|分页页码 （非必传，默认为1）
     * @param limit|每页显示行数 （非必传，默认为10）
     * @return mixed
     *
     */
    public MsgRec getMsgRecList(MsgRec msgRec){
        return msgInboxMapper.getMsgRecList(msgRec);
    }

    /**
     * 发件箱列表查询
     * @access public
     * @param user_id|用户ID(发件人)
     * @param user_type|用户类型(发件人)
     * @param page|分页页码 （非必传，默认为1）
     * @param limit|每页显示行数 （非必传，默认为10）
     * @param message_status|站内信状态:未读 0/已读 1 （非必传，默认为0）
     * @return mixed
     *
     */
    public MsgSys getMsgSysList(MsgSys msgSys){
        return msgInboxMapper.getMsgSysList(msgSys);
    }

    /**
     * 用户未接收信息查询
     * @access public
     * @param user_id|用户ID(发件人)
     * @param user_type|用户类型(发件人)
     * @param page|分页页码 （非必传，默认为1）
     * @param limit|每页显示行数 （非必传，默认为10）
     * @param message_status|站内信状态:未读 0/已读 1 （非必传，默认为0）
     * @return mixed
     *
     */
    public MsgSys getMsgSysALL(MsgSys msgSys){
        return msgInboxMapper.getMsgSysALL(msgSys);
    }

    /**
     * 站内信详情查询
     * @access public
     * @param msg_id|站内信id
     * @return mixed
     *
     */
    public MsgInbox getMsgInfo(long msg_id){
        return msgInboxMapper.getMsgInfo(msg_id);
    }

    /**
     * 删除/读取站内信（update）
     * @access public
     * @param msg_id|站内信id
     * @param update_data|更新数据
     * ['deleted_at'=>time()] 删除
     * ['sys_status'=>'1'] 标记已读
     * @return mixed
     */
    public int updateMsgRec(MsgUpdate msgUpdate){
        return msgInboxMapper.updateMsgRec(msgUpdate);
    }

    /**
     * 收取站内信（系统把站内信同步到用户收件箱,同时修改系统发件表的状态）
     * @access public
     * @param msg_id|消息标识列
     * @param user_id|用户ID(收件人)
     * @param user_type|发件人类型，个人用户1/企业用户2/管理员等3
     * @param sys_status|消息状态：默认为未读0/已读1
     * @return mixed
     *
     */
    public int insertMsgRec(MsgRec msgRec){
        return msgInboxMapper.insertMsgRec(msgRec);
    }

}
