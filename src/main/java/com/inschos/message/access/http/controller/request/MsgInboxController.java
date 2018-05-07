package com.inschos.message.access.http.controller.request;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.inschos.message.access.http.controller.action.MsgInboxAction;
import com.inschos.message.kit.HttpKit;
import com.inschos.message.model.MsgInbox;
import com.inschos.message.model.MsgRec;
import com.inschos.message.model.MsgSys;
import com.inschos.message.model.MsgUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.jar.JarEntry;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/message")
public class MsgInboxController {

    private static ObjectMapper mapper = null;

    @Autowired
    private MsgInboxAction msgInboxAction;

    //收件箱
    @RequestMapping("/list/inbox/**")
    @ResponseBody
    public String listInbox(HttpServletRequest request) {
        String body = HttpKit.readRequestBody(request);
        return msgInboxAction.getMsgRecList(body);
    }

    //发件箱
    @RequestMapping("/list/outbox/**")
    @ResponseBody
    public String listOutbox(HttpServletRequest request) {
        String body = HttpKit.readRequestBody(request);
        return msgInboxAction.getMsgSysList(body);
    }

    //站内信详情
    @RequestMapping("/info/**")
    @ResponseBody
    public String infoMessage(HttpServletRequest request) {
        String body = HttpKit.readRequestBody(request);
        return msgInboxAction.getMsgInfo(body);
    }

    //操作站内信
    @RequestMapping("/operate/**")
    @ResponseBody
    public String operateMessage(HttpServletRequest request) {
        String body = HttpKit.readRequestBody(request);
        return msgInboxAction.updateMsgRec(body);
    }
}
