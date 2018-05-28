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
     * 消息 收件箱列表-总列表
     *
     * @param userId        用户id
     * @param userType      用户类型:个人用户 1/企业用户 2//代理人 3/业管用户4
     * @param messageStatus 消息 状态:未读1/已读2/全部3/删除4（非必传，默认为1）
     * @param pageNum       当前页码 ，可不传，默认为1
     * @param lastId        上一页最大id ，可不传，默认为
     * @param limit         每页显示行数，可不传，默认为
     * @return json
     * @access public
     */
    @GetActionBeanAnnotation(isCheckAccess = false)
    @RequestMapping("/list/inbox/**")
    @ResponseBody
    public String listInbox(ActionBean actionBean) {
        return msgInboxAction.findMsgRecList(actionBean);
    }

    /**
     * 消息 收件箱列表-某一分类列表
     *
     * @param userId        用户id
     * @param userType      用户类型:个人用户 1/企业用户 2//代理人 3/业管用户4
     * @param messageStatus 消息 状态:未读 1/已读 2/全部 3/（非必传，默认为1）
     * @param messageType   消息 类型:系统通知1/保单助手2/理赔进度3/最新任务4/客户消息5/活动消息6/顾问消息7/'
     * @param pageNum       当前页码 ，可不传，默认为1
     * @param lastId        上一页最大id ，可不传，默认为
     * @param limit         每页显示行数，可不传，默认为
     * @return json
     * @access public
     */
    @GetActionBeanAnnotation(isCheckAccess = false)
    @RequestMapping("/list/inbox/type/**")
    @ResponseBody
    public String listInboxByType(ActionBean actionBean) {
        return msgInboxAction.findMsgResListByType(actionBean);
    }

    /**
     * 消息 根据parentId获取消息列表
     *
     * @param userId        用户id
     * @param userType      用户类型:个人用户 1/企业用户 2//代理人 3/业管用户4
     * @param messageStatus 消息 状态:未读 1/已读 2/全部 3/（非必传，默认为1）
     * @param messageType   消息 类型:系统通知1/保单助手2/理赔进度3/最新任务4/客户消息5/活动消息6/顾问消息7/'
     * @param parentId
     * @param pageNum       当前页码 ，可不传，默认为1
     * @param lastId        上一页最大id ，可不传，默认为
     * @param limit         每页显示行数，可不传，默认为
     * @return json
     * @access public
     */
    @GetActionBeanAnnotation(isCheckAccess = false)
    @RequestMapping("/list/inbox/parent/**")
    @ResponseBody
    public String listInboxByParent(ActionBean actionBean) {
        return msgInboxAction.findMsgResListByParent(actionBean);
    }

    /**
     * 消息 发件箱列表
     *
     * @param userId        用户id
     * @param userType      用户类型:个人用户 1/企业用户 2//代理人 3/业管用户4
     * @param messageStatus 消息 状态:未读1/已读2/全部3/删除4（非必传，默认为1）
     * @param pageNum       当前页码 ，可不传，默认为1
     * @param lastId        上一页最大id ，可不传，默认为
     * @param limit         每页显示行数，可不传，默认为
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
     * 消息 发件箱列表-某一分类列表
     *
     * @param userId        用户id
     * @param userType      用户类型:个人用户 1/企业用户 2//代理人 3/业管用户4
     * @param messageStatus 消息 状态:未读 1/已读 2/全部 3/（非必传，默认为1）
     * @param messageType   消息 类型:系统通知1/保单助手2/理赔进度3/最新任务4/客户消息5/活动消息6/顾问消息7/'
     * @param pageNum       当前页码 ，可不传，默认为1
     * @param lastId        上一页最大id ，可不传，默认为
     * @param limit         每页显示行数，可不传，默认为
     * @return json
     * @access public
     */
    @GetActionBeanAnnotation(isCheckAccess = false)
    @RequestMapping("/list/outbox/type/**")
    @ResponseBody
    public String listOutboxByType(ActionBean actionBean) {
        return msgInboxAction.findMsgSysListByType(actionBean);
    }

    /**
     * 消息 根据parentId获取消息列表
     *
     * @param userId        用户id
     * @param userType      用户类型:个人用户 1/企业用户 2//代理人 3/业管用户4
     * @param messageStatus 消息 状态:未读 1/已读 2/全部 3/（非必传，默认为1）
     * @param messageType   消息 类型:系统通知1/保单助手2/理赔进度3/最新任务4/客户消息5/活动消息6/顾问消息7/'
     * @param parentId
     * @param pageNum       当前页码 ，可不传，默认为1
     * @param lastId        上一页最大id ，可不传，默认为
     * @param limit         每页显示行数，可不传，默认为
     * @return json
     * @access public
     */
    @GetActionBeanAnnotation(isCheckAccess = false)
    @RequestMapping("/list/outbox/parent/**")
    @ResponseBody
    public String listOutboxByParent(ActionBean actionBean) {
        return msgInboxAction.findMsgSysListByParent(actionBean);
    }

    /**
     * 消息 详情
     *
     * @param messageId 消息 id
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
     * @param messageId   消息 id
     * @param operateId   操作代码:默认为1（删除/已读），2（还原/未读）
     * @param operateType 操作类型:read 更改读取状态，del 更改删除状态
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
