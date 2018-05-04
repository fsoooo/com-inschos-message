package com.inschos.message.access.http.controller.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MsgModelAction {
    //@Autowired
    //private ModelDao modelDao;

    /**
     * 添加站内信模板
     * @access public
     * @param model_name|string 模板名称（不能一样）
     * @param model_content|string 模板内容
     * @param created_user|string 创建者姓名
     * @param created_user_type|string  创建者类型
     * @return json
     */
//    public String addModel(String model_name,String model_content,String created_user,String created_user_type) {
//
//    }

    /**
     * 站内信模板列表
     * @access public
     * @param page|string  分页页码，可为空，默认为1
     * @param model_status|string  模板状态（审核通过0/未通过1/已删除2）
     * @return json
     */
//    public String listModel() {
//
//    }

    /**
     * 站内信模板详情
     * @access public
     * @param model_code|string  模板代码
     * @return json
     */
//    public String infoModel() {
//
//    }

    /**
     * 站内信模板操作（审核、删除）
     * @access public
     * @param model_code|string  模板代码
     * @param operate_code|string  操作代码（审核通过1，删除2）
     * @param user_id|string  操作人id
     * @param user_type|string  操作人类型（只有业管可以审核和删除）
     * @return json
     */
//    public String operateModel() {
//
//    }
}
