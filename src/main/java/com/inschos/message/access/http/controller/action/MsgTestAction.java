package com.inschos.message.access.http.controller.action;


import com.inschos.message.access.http.controller.bean.*;
import com.inschos.message.access.rpc.bean.AccountBean;
import com.inschos.message.access.rpc.bean.AgentJobBean;
import com.inschos.message.access.rpc.bean.MessageBean;
import com.inschos.message.access.rpc.client.*;
import com.inschos.message.access.rpc.client.AgentJobClient;
import com.inschos.message.access.rpc.client.ChannelClient;
import com.inschos.message.assist.kit.JsonKit;
import com.inschos.message.assist.kit.L;
import com.inschos.message.assist.kit.TimeKit;
import com.inschos.message.data.dao.MsgInboxDAO;
import com.inschos.message.data.dao.MsgIndexDAO;
import com.inschos.message.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class MsgTestAction extends BaseAction {
    private static final Logger logger = Logger.getLogger(MsgTestAction.class);

    @Autowired
    private MessageClient messageClient;

    public String sendMessage(ActionBean actionBean){
        MsgIndexBean request = JsonKit.json2Bean(actionBean.body, MsgIndexBean.class);
        BaseResponse response = new BaseResponse();
        MessageBean.Request messageRequest = new MessageBean.Request();
        messageRequest.title = "测试rpc";
        messageRequest.content = "测试rpc";
        messageRequest.attachment = "";
        messageRequest.type = 1;
        messageRequest.fromId = 2;
        messageRequest.fromType = 6;
        messageRequest.sendTime = "";
        messageRequest.businessId = "-1";
        messageRequest.managerUuid = "2";
        messageRequest.accountUuid = "2";
        messageRequest.sysId = 1;
        List<AddMsgToBean> toUser = new ArrayList<>();
        AddMsgToBean addMsgToBean = new AddMsgToBean();
        addMsgToBean.toId = 2;
        addMsgToBean.toType = 4;
        addMsgToBean.toName = "";
        addMsgToBean.channelId = 0;
        addMsgToBean.channelName = "";
        toUser.add(addMsgToBean);
        messageRequest.toUser = toUser;
        MessageBean.Response messageResponse = messageClient.sendMessage(messageRequest);
        logger.info(messageResponse);
        if(messageResponse!=null){
            response.data = messageResponse.message;
        }
        return json(BaseResponse.CODE_FAILURE, "请求RPC成功", response);
    }
}
