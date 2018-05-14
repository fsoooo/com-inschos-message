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
public class MsgIndexController {

    private static final Logger logger = Logger.getLogger(MsgModelController.class);
    @Autowired
    private MsgIndexAction msgIndexAction;

    /**
     * 发送站内信
     *
     * @param $from|int         发件人id
     * @param $from_type|string 发件人类型:个人用户/企业用户/管理员等
     * @param $to|array         收件人id
     * @param $to_type|sting    收件人类型;个人用户/企业用户/管理员等
     * @param $title|string     主题
     * @param $body|string      内容
     * @param $type|string      站内信类型
     * @param $file|resource    附件 可空
     * @param $send_time|string 发送时间 可空
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
