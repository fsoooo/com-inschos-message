package com.inschos.message.access.http.controller.request;

import com.inschos.message.access.http.controller.action.MsgIndexAction;
import com.inschos.message.access.http.controller.bean.ActionBean;
import com.inschos.message.annotation.GetActionBeanAnnotation;
import com.inschos.message.assist.kit.HttpKit;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * User: wangsl
 * Date: 2018/05/11
 * Time: 17:12
 * 发送消息逻辑:系统消息，订阅消息，私信，可以上传附件
 */
@Controller
@RequestMapping("/message")
//TODO 路由统一也用小驼峰命名规则
public class MsgIndexController {

    private static final Logger logger = Logger.getLogger(MsgModelController.class);
    @Autowired
    private MsgIndexAction msgIndexAction;

    /**
     * 发送消息
     *
     * @param title|标题
     * @param content|内容
     * @param attachment|附件:上传附件的URL,可为空
     * @param type|消息 类型:系统通知1/保单助手2/理赔进度3/最新任务4/客户消息5/活动消息6/顾问消息7/
     * @param fromId|发件人ID
     * @param fromType|发件人类型:个人用户1/企业用户2/业管用户等3
     * @param toId|收件人id
     * @param toType|收件人类型:个人用户1/企业用户2/业管用户等3
     * @param status|读取状态:标识消息 是否已被读取,未读0/已读1.避免重复向收件箱表插入数据,默认为0
     * @param sendTime|发送时间:默认为空。需要延时发送的，发送时间不为空
     * @param parentId|消息父级id
     * @return json
     * @access public
     */
    @GetActionBeanAnnotation(isCheckAccess = false)
    @RequestMapping("/add/**")
    @ResponseBody
    public String addMessage(ActionBean actionBean) {
        return msgIndexAction.addMessage(actionBean);
    }
}
