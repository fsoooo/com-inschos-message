package com.inschos.message.access.http.controller.request;

import com.inschos.message.access.http.controller.action.MsgModelAction;
import com.inschos.message.kit.HttpKit;
import com.inschos.message.model.MsgModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * User: wangsl
 * Date: 2018/05/04
 * Time: 17:12
 * 站内信模板管理主要功能：添加，查询列表，查询详情，操作（审核通过，删除）
 */
@Controller
@RequestMapping("/message/model")
public class MsgModelController
{
    @Autowired
    private MsgModelAction msgModelAction;
    private static final Logger logger = Logger.getLogger(MsgModelController.class);

    /**
     * 添加站内信模板
     * @access public
     * @param model_name|string 模板名称（不能一样）
     * @param model_content|string 模板内容
     * @param created_user|string 创建者姓名
     * @param created_user_type|string  创建者类型
     * @return json
     */
    @RequestMapping("/add/**")
    @ResponseBody
    public String addMsgModel(HttpServletRequest request){
        String body = HttpKit.readRequestBody(request);
        return msgModelAction.addMsgModel(body);
    }

    /**
     * 站内信模板列表
     * @access public
     * @param Page|string  分页页码，可为空，默认为1
     * @param model_status|string  模板状态（审核通过0/未通过1/已删除2）
     * @return json
     */
    @RequestMapping("/list/**")
    @ResponseBody
    public String listMsgModel(HttpServletRequest request){
        String body = HttpKit.readRequestBody(request);
        return msgModelAction.listMsgModel(body);
    }

    /**
     * 站内信模板详情
     * @access public
     * @param model_code|string  模板代码
     * @return json
     */
    @RequestMapping("/info/**")
    @ResponseBody
    public String infoMsgModel(HttpServletRequest request){
        String body = HttpKit.readRequestBody(request);
        return msgModelAction.infoMsgModel(body);
    }

    /**
     * 站内信模板操作（审核、删除）
     * @access public
     * @param model_code|string  模板代码
     * @param operate_code|string  操作代码（审核通过1，删除2）
     * @param user_id|string  操作人id
     * @param user_type|string  操作人类型（只有业管可以审核和删除）
     * @return json
     */
    @RequestMapping("/update/**")
    @ResponseBody
    public String updateMsgModel(HttpServletRequest request){
        String body = HttpKit.readRequestBody(request);
        return msgModelAction.updateMsgModel(body);
    }
}
