package com.inschos.message.access.http.controller.request;

import com.inschos.message.access.http.controller.action.MsgInboxAction;
import com.inschos.message.model.MsgInbox;
import com.inschos.message.model.MsgRec;
import com.inschos.message.model.MsgSys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/message")
public class MsgInboxController {
    @Autowired
    private MsgInboxAction msgInboxAction;

    //收件箱
    @RequestMapping("/list/inbox/**")
    @ResponseBody
    public MsgRec listInbox(MsgRec msgRec) {
        return msgInboxAction.getMsgRecList(msgRec);
    }

    //发件箱
    @RequestMapping("/list/outbox/**")
    @ResponseBody
    public MsgSys listOutbox(MsgSys msgSys) {
        return msgInboxAction.getMsgSysList(msgSys);
    }

    //站内信详情
    @RequestMapping("/info/**")
    @ResponseBody
    public MsgInbox infoMessage(long msg_id){
        return msgInboxAction.getMsgInfo(msg_id);
    }


}
