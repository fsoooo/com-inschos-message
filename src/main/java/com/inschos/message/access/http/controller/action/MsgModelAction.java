package com.inschos.message.access.http.controller.action;

import com.inschos.message.access.http.controller.bean.BaseRequest;
import com.inschos.message.access.http.controller.bean.BaseResponse;
import com.inschos.message.data.dao.*;
import com.inschos.message.kit.JsonKit;
import com.inschos.message.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MsgModelAction extends BaseAction {
    @Autowired
    private MsgModelDAO msgModelDAO;
    private Page page;
    private static final Logger logger = Logger.getLogger(MsgModelAction.class);
    /**
     * 添加站内信模板
     * @access public
     * @param model_name|string 模板名称（不能一样）
     * @param model_content|string 模板内容
     * @param created_user|string 创建者姓名
     * @param created_user_type|string  创建者类型
     * @return json
     */
    public String addMsgModel(String body) {
        MsgModel msgModel = JsonKit.json2Bean(body, MsgModel.class);
        BaseResponse response = new BaseResponse();
        if(msgModel==null){
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        //获取传进来的参数
        MsgModel request = requst2Bean(msgModel.model_content, MsgModel.class);
        logger.info(msgModel);
        logger.info(request);
        logger.info(msgModel.model_code);
        logger.info(msgModel.model_name);
        logger.info(msgModel.model_content);
        int add_res = msgModelDAO.addMsgModel(msgModel);
        return json(BaseResponse.CODE_FAILURE, "业务完善中", response);
    }

    /**
     * 站内信模板列表
     * @access public
     * @param Page|string  分页页码，可为空，默认为1
     * @param model_status|string  模板状态（审核通过0/未通过1/已删除2）
     * @return json
     */
    public String listMsgModel(String body) {
        Page page = JsonKit.json2Bean(body, Page.class);
        if(page!=null){
            BaseRequest request = requst2Bean(page.data, BaseRequest.class);
            BaseResponse response = new BaseResponse();
            return json(BaseResponse.CODE_FAILURE, "业务完善中", response);
        }else{
            return "params is empty";
        }
    }

    /**
     * 站内信模板详情
     * @access public
     * @param model_code|string  模板代码
     * @return json
     */
    public String infoMsgModel(String body) {
        MsgModel msgModel = JsonKit.json2Bean(body, MsgModel.class);
        if(page!=null){
            BaseRequest request = requst2Bean(msgModel.model_code, BaseRequest.class);
            BaseResponse response = new BaseResponse();
            return json(BaseResponse.CODE_FAILURE, "业务完善中", response);
        }else{
            return "params is empty";
        }
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
    public String updateMsgModel(String body) {
        MsgModelUpdate msgModelUpdate = JsonKit.json2Bean(body, MsgModelUpdate.class);
        if(msgModelUpdate!=null){
            BaseRequest request = requst2Bean(msgModelUpdate.update_data, BaseRequest.class);
            BaseResponse response = new BaseResponse();
            return json(BaseResponse.CODE_FAILURE, "业务完善中", response);
        }else{
            return "params is empty";
        }
    }
}
