package com.inschos.message.access.http.controller.request;

import com.inschos.message.access.http.controller.action.MsgModelAction;
import com.inschos.message.access.http.controller.bean.ActionBean;
import com.inschos.message.annotation.GetActionBeanAnnotation;
import com.inschos.message.assist.kit.HttpKit;
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
public class MsgModelController {
    private static final Logger logger = Logger.getLogger(MsgModelController.class);
    @Autowired
    private MsgModelAction msgModelAction;

    /**
     * 添加站内信模板
     *
     * @param model_name        模板名称（不能一样）
     * @param model_content     模板内容
     * @param created_user      创建者姓名
     * @param created_user_type 创建者类型
     * @return json
     * @access public
     */
    @GetActionBeanAnnotation(isCheckAccess = false)
    @RequestMapping("/add/**")
    @ResponseBody
    public String addMsgModel(ActionBean actionBean) {
        return msgModelAction.addMsgModel(actionBean);
    }

    /**
     * 站内信模板列表
     *
     * @param page         当前页码 ，可不传，默认为1
     * @param last_id      上一页最大id ，可不传，默认为
     * @param limit        每页显示行数，可不传，默认为
     * @param model_status 模板状态（审核通过0/未通过1/已删除2）
     * @return json
     * @access public
     */
    @GetActionBeanAnnotation(isCheckAccess = false)
    @RequestMapping("/list/**")
    @ResponseBody
    public String listMsgModel(ActionBean actionBean) {
        return msgModelAction.listMsgModel(actionBean);
    }


    /**
     * 站内信模板详情
     *
     * @param model_code 模板代码
     * @return json
     * @access public
     */
    @GetActionBeanAnnotation(isCheckAccess = false)
    @RequestMapping("/info/**")
    @ResponseBody
    public String infoMsgModel(ActionBean actionBean) {
        return msgModelAction.infoMsgModel(actionBean);
    }

    /**
     * 站内信模板操作（审核、删除）
     *
     * @param model_code   模板代码
     * @param operate_code '审核状态:默认为0审核中，1审核通过，2审核失败'
     * @param user_id      操作人id
     * @param user_type    操作人类型（只有业管可以审核和删除）
     * @return json
     * @access public
     */
    @GetActionBeanAnnotation(isCheckAccess = false)
    @RequestMapping("/update/**")
    @ResponseBody
    public String updateMsgModel(ActionBean actionBean) {
        return msgModelAction.updateMsgModel(actionBean);
    }
}
