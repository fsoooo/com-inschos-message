package com.inschos.message.access.rpc.service.impl;

import com.inschos.message.access.http.controller.action.MsgIndexAction;
import com.inschos.message.access.http.controller.bean.AddMsgRecord;
import com.inschos.message.access.http.controller.bean.BaseResponse;
import com.inschos.message.access.rpc.bean.AccountBean;
import com.inschos.message.access.rpc.bean.AgentJobBean;
import com.inschos.message.access.rpc.bean.MessageBean;
import com.inschos.message.access.rpc.client.AccountClient;
import com.inschos.message.access.rpc.client.AgentJobClient;
import com.inschos.message.access.rpc.client.ChannelClient;
import com.inschos.message.access.rpc.service.MessageService;
import com.inschos.message.assist.kit.JsonKit;
import com.inschos.message.assist.kit.L;
import com.inschos.message.data.dao.MsgInboxDAO;
import com.inschos.message.data.dao.MsgIndexDAO;
import com.inschos.message.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    private static final Logger logger = Logger.getLogger(MessageServiceImpl.class);
    @Autowired
    private MsgIndexDAO msgIndexDAO;
    @Autowired
    private MsgInboxDAO msgInboxDAO;

    @Autowired
    private ChannelClient channelClient;
    @Autowired
    private AgentJobClient agentJobClient;
    @Autowired
    private AccountClient accountClient;
    @Autowired
    private MsgIndexAction msgIndexAction;

    @Override
    public MessageBean.Response sendMessage(MessageBean.Request request) {
        logger.info("执行rpc中-start");
        MessageBean.Response response = new MessageBean.Response();
        if (request == null) {
            response.code = 500;
            response.message = "参数解析失败";
            return response;
        }
        if (request.title == null || request.content == null) {
            response.code = 500;
            response.message = "标题和内容不能为空";
            return response;
        }
        if (request.toUser == null || request.toUser.size() == 0) {
            response.code = 500;
            response.message = "发送对象不能为空";
            return response;
        }
        //通过rpc发送消息的现在只有系统触发
        logger.info("执行rpc中");
        long date = new Date().getTime();
        MsgSys msgSys = new MsgSys();
        msgSys.manager_uuid = request.managerUuid;
        msgSys.account_uuid = request.accountUuid;
        msgSys.business_id = request.businessId;
        msgSys.title = request.title;
        msgSys.content = request.content;
        msgSys.type = request.type;
        if (request.attachment == null) {
            request.attachment = "";
        }
        msgSys.attachment = request.attachment;
        msgSys.send_time = request.sendTime;
        msgSys.from_id = request.fromId;
        msgSys.from_type = request.fromType;
        msgSys.created_at = date;
        msgSys.updated_at = date;
        AddMsgRecord addMsgRecord = new AddMsgRecord();
        int sendRes = msgIndexDAO.addMessage(msgSys);
        logger.info(sendRes);
        if (sendRes > 0) {
            addMsgRecord.messageId = msgSys.id;
            String addMsgRes = "";
            addMsgRes = addMsgRecord(addMsgRecord,request.toUser);
            logger.info(addMsgRes);
            if (addMsgRes != null) {
                response.code = 200;
                response.message = "发送成功";
                return response;
            } else {
                response.code = 500;
                response.message = "发送失败";
                return response;
            }
        } else {
            response.code = 500;
            response.message = "发送失败";
            return response;
        }
    }

    private String addMsgRecord(AddMsgRecord addMsgRecord,List<MessageBean.AddMsgToBean> toUser){
        MessageBean.Response response = new MessageBean.Response();
        long date = new Date().getTime();
        for (MessageBean.AddMsgToBean addMsgToBean : toUser) {
            MsgRecord msgRecord = new MsgRecord();
            msgRecord.msg_id = addMsgRecord.messageId;
            msgRecord.rec_id = addMsgToBean.toId;
            msgRecord.type = addMsgToBean.toType;
            msgRecord.state = 1;
            msgRecord.status = 1;
            msgRecord.created_at = date;
            msgRecord.updated_at = date;
            RepeatCount msgRecordRepeat = msgIndexDAO.findAddMsgRecordRepeat(msgRecord);//发件记录表
            if(msgRecordRepeat.count==0){
                int addMsgRec = msgIndexDAO.addMessageRecord(msgRecord);//发件记录表
                logger.info("添加发件记录"+addMsgRec);
            }
        }
        response.code = 200;
        response.message = "发送成功";
        return JsonKit.bean2Json(response);
    }
}
