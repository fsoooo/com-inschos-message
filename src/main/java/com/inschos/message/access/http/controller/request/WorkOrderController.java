package com.inschos.message.access.http.controller.request;

import com.inschos.message.access.http.controller.action.WorkOrderAction;
import com.inschos.message.access.http.controller.bean.BaseResponse;
import com.inschos.message.annotation.GetActionBeanAnnotation;
import com.inschos.message.assist.kit.HttpKit;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


/**
 * User: changyy
 * Date: 2018/05/19
 * Time: 17:31
 * 工单管理主要功能：创建工单，工单列表，工单详情，回复工单（关闭），工单状态流转
 */
@Controller
@RequestMapping("/work/")
public class WorkOrderController {
    private static final Logger logger = Logger.getLogger(WorkOrderController.class);
    @Autowired
    private WorkOrderAction workOrderAction;

    /**
     * 创建工单
     *
     * @param name        工单标题
     * @param classify     类型
     * @param content      内容
     * @return json
     * @access public
     */
    @GetActionBeanAnnotation(isCheckAccess = false)
    @RequestMapping("/add/**")
    @ResponseBody
    public String addWork(HttpServletRequest request){

        BaseResponse response = new BaseResponse();

        return workOrderAction.addWork(request);

    }

}
