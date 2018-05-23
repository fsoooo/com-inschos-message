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

/**
 * User: wangsl
 * Date: 2018/05/11
 * Time: 17:12
 * 消息 收件箱主要功能：查询列表(收件箱、发件箱)，查询详情，操作（已读，删除）
 */
@Controller
@RequestMapping("/message")
public class MsgInboxController {
    private static final Logger logger = Logger.getLogger(MsgInboxController.class);
    @Autowired
    private MsgInboxAction msgInboxAction;

    /**
     * 消息 收件箱列表
     *
     * @param user_id            用户id
     * @param user_type       用户类型:个人用户 3/代理人 2/企业用户 1/业管用户
     * @param message_status  消息 状态:未读 0/已读 1/全部 2/删除 3 （非必传，默认为0）
     * @param page                  当前页码 ，可不传，默认为1
     * @param last_id               上一页最大id ，可不传，默认为
     * @param limit                 每页显示行数，可不传，默认为
     * @return json
     * <p>
     * 业管可以查看所有人的消息
     * 消息 列表组成：消息 系统表里收件人id为0的（系统消息）+ 消息 系统表里收件人id为user_id的（订阅消息、私信）
     * 匹配消息 系统表和消息 收件箱表，向用户收件箱里插入相应的数据，并修改消息 系统表的状态
     * todo 只要用户接收消息 ，系统表就默认已经读取了，不在插入
     * @access public
     */
    @GetActionBeanAnnotation(isCheckAccess = false)
    @RequestMapping("/list/inbox/**")
    @ResponseBody
    public String listInbox(ActionBean actionBean) {
        return msgInboxAction.findMsgRecList(actionBean);
    }

    /**
     * 消息 发件箱列表
     *
     * @param user_id            用户id
     * @param user_type       用户类型:个人用户 3/代理人 2/企业用户 1/业管用户 0
     * @param message_status  消息 状态:未读 0/已读 1/全部 2/删除 3 （非必传，默认为0）
     * @param page                  当前页码 ，可不传，默认为1
     * @param last_id               上一页最大id ，可不传，默认为
     * @param limit                 每页显示行数，可不传，默认为
     * @return json
     * @access public
     */
    @GetActionBeanAnnotation(isCheckAccess = false)
    @RequestMapping("/list/outbox/**")
    @ResponseBody
    public String listOutbox(ActionBean actionBean) {
        return msgInboxAction.findMsgSysList(actionBean);
    }

    /**
     * 消息 详情
     *
     * @param message_id    消息 id
     * @return json
     * @access public
     */
    @GetActionBeanAnnotation(isCheckAccess = false)
    @RequestMapping("/info/**")
    @ResponseBody
    public String infoMessage(ActionBean actionBean) {
        return msgInboxAction.findMsgInfo(actionBean);
    }

    /**
     * 操作消息 （收件箱 读取和删除）
     *
     * @param message_id      消息 id
     * @param operate_id      操作代码:默认为1（删除/已读），2（还原/未读）
     * @param operate_type    操作类型:read 更改读取状态，del 更改删除状态
     * @return json
     * @access public
     */
    @GetActionBeanAnnotation(isCheckAccess = false)
    @RequestMapping("/update/**")
    @ResponseBody
    public String updateMessage(ActionBean actionBean) {
        return msgInboxAction.updateMsgRec(actionBean);
    }
}
