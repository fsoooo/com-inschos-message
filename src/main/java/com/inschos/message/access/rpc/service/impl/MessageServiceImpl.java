package com.inschos.message.access.rpc.service.impl;

import com.inschos.message.access.http.controller.action.MsgIndexAction;
import com.inschos.message.access.http.controller.bean.AddMsgRecord;
import com.inschos.message.access.rpc.bean.AgentJobBean;
import com.inschos.message.access.rpc.bean.MessageBean;
import com.inschos.message.access.rpc.client.AccountClient;
import com.inschos.message.access.rpc.client.AgentJobClient;
import com.inschos.message.access.rpc.client.ChannelClient;
import com.inschos.message.access.rpc.service.MessageService;
import com.inschos.message.data.dao.MsgInboxDAO;
import com.inschos.message.data.dao.MsgIndexDAO;
import com.inschos.message.model.MsgStatus;
import com.inschos.message.model.MsgSys;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    private static final Logger logger = Logger.getLogger(MsgIndexAction.class);
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
        MsgStatus msgStatus = new MsgStatus();
        //权限判断 个人1/企业2/代理人3/业管4/渠道5/系统6
        if (request.toUser.size() > 1 && request.fromType == msgStatus.USER_PERSON) {
            response.code = 500;
            response.message = "您没有权限执行这项操作";
            return response;
        }
        //TODO 当系统需要发送消息,managerUuid、accountUuid、都等于sysId.
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
        addMsgRecord.toUser = request.toUser;
        List<AgentJobBean> agentJobBeans = msgIndexAction.findChannelUser(addMsgRecord, request.sysId, request.managerUuid);
        logger.info(agentJobBeans.size());
        if (agentJobBeans.size() == 0) {
            response.code = 500;
            response.message = "消息接收人为空,发送失败";
            return response;
        }
        int send_result = msgIndexDAO.addMessage(msgSys);
        if (send_result > 0) {
            addMsgRecord.messageId = msgSys.id;
            String addMsgRes = msgIndexAction.addMsgRecord(addMsgRecord, request.sysId, request.managerUuid, agentJobBeans);
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
}
