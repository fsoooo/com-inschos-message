package com.inschos.message.data.dao;

import com.inschos.message.data.mapper.MsgIndexMapper;
import com.inschos.message.data.mapper.MsgModelMapper;
import com.inschos.message.model.MsgRec;
import com.inschos.message.model.MsgSys;
import org.springframework.beans.factory.annotation.Autowired;

public class MsgIndexDAO {
    @Autowired
    private MsgIndexMapper msgIndexMapper;
    /**
     * 发送站内信
     * @access public
     * @param title|标题
     * @param content|内容
     * @param attachment|附件:上传附件的URL,可为空
     * @param type|站内信类型:系统通知、保单消息、理赔消息，其他（站内信分类，可为空）
     * @param from_id|发件人ID
     * @param from_type|发件人类型:个人用户1/企业用户2/管理员等3
     * @param to_id|收件人id
     * @param to_type|收件人类型:个人用户1/企业用户2/管理员等3
     * @param status|读取状态:标识站内信是否已被读取,未读0/已读1.避免重复向收件箱表插入数据,默认为0
     * @param send_time|发送时间:默认为空。需要延时发送的，发送时间不为空
     * @return mixed
     *
     */
    public int addMsgSys(MsgSys msgSys){
        return msgIndexMapper.addMsgSys(msgSys);
    }

}
