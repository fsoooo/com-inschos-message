package com.inschos.message.access.http.controller.request;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.inschos.message.access.http.controller.action.MsgInboxAction;
import com.inschos.message.access.http.controller.bean.ActionBean;
import com.inschos.message.annotation.GetActionBeanAnnotation;
import com.inschos.message.assist.kit.HttpKit;
import com.inschos.message.model.MsgInbox;
import com.inschos.message.model.MsgRec;
import com.inschos.message.model.MsgSys;
import com.inschos.message.model.MsgUpdate;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.jar.JarEntry;

@Controller
@RequestMapping("/message")
public class MsgInboxController {
    @Autowired
    private MsgInboxAction msgInboxAction;
    private static final Logger logger = Logger.getLogger(MsgInboxController.class);

    /**
     * 站内信收件箱列表
     * @access public
     * @param $user_id|int  用户id
     * @param $user_type|string 用户类型:个人用户 3/代理人 2/企业用户 1/管理员
     * @param $page|string  分页页码 （非必传，默认为1）
     * @param $message_status|string  站内信状态:未读 0/已读 1/全部 2/删除 3 （非必传，默认为0）
     * @return json
     *
     * 业管可以查看所有人的站内信
     * 站内信列表组成：站内信系统表里收件人id为0的（系统消息）+ 站内信系统表里收件人id为user_id的（订阅消息、私信）
     * 匹配站内信系统表和站内信收件箱表，向用户收件箱里插入相应的数据，并修改站内信系统表的状态
     * todo 只要用户接收站内信，系统表就默认已经读取了，不在插入
     */
    @GetActionBeanAnnotation(isCheckAccess = false)
    @RequestMapping("/list/inbox/**")
    @ResponseBody
    public String listInbox(ActionBean actionBean) {
        return msgInboxAction.getMsgRecList(actionBean);
    }

    /**
     * 站内信发件箱列表
     * @access public
     * @param $user_id|int  用户id
     * @param $user_type|string 用户类型:个人用户 3/代理人 2/企业用户 1/管理员 0
     * @param $page|string  分页页码 （非必传，默认为1）
     * @return json
     *
     */
    @GetActionBeanAnnotation(isCheckAccess = false)
    @RequestMapping("/list/outbox/**")
    @ResponseBody
    public String listOutbox(ActionBean actionBean) {
        return msgInboxAction.getMsgSysList(actionBean);
    }

    /**
     * 站内信详情
     * @access public
     * @param $message_id |string  站内信id
     * @return json
     */
    @GetActionBeanAnnotation(isCheckAccess = false)
    @RequestMapping("/info/**")
    @ResponseBody
    public String infoMessage(ActionBean actionBean) {
        return msgInboxAction.getMsgInfo(actionBean);
    }

    /**
     * 操作站内信（收件箱 读取和删除）
     * @access public
     * @param $message_id |string  站内信id
     * @param $operate_id |string  操作代码:默认为1（删除/已读），2（还原/未读）
     * @param $operate_type |string  操作类型:read 更改读取状态，del 更改删除状态
     * @return json
     */
    @GetActionBeanAnnotation(isCheckAccess = false)
    @RequestMapping("/update/**")
    @ResponseBody
    public String updateMessage(ActionBean actionBean) {
        return msgInboxAction.updateMsgRec(actionBean);
    }
}
