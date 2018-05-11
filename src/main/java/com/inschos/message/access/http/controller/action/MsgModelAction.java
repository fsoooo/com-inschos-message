package com.inschos.message.access.http.controller.action;

import com.inschos.message.access.http.controller.bean.ActionBean;
import com.inschos.message.access.http.controller.bean.BaseRequest;
import com.inschos.message.access.http.controller.bean.BaseResponse;
import com.inschos.message.access.http.controller.bean.MsgModelBean;
import com.inschos.message.data.dao.*;
import com.inschos.message.assist.kit.JsonKit;
import com.inschos.message.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class MsgModelAction extends BaseAction {
    private static final Logger logger = Logger.getLogger(MsgModelAction.class);
    @Autowired
    private MsgModelDAO msgModelDAO;
    private Page page;

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
    public String addMsgModel(String body) {
        MsgModelBean.msgModelAdd msgModelAdd = JsonKit.json2Bean(body, MsgModelBean.msgModelAdd.class);
        //获取传进来的参数
        MsgModelBean.msgModelAdd request = requst2Bean(msgModelAdd.model_content, MsgModelBean.msgModelAdd.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (msgModelAdd == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        //获取当前时间戳(毫秒值)
        long date = new Date().getTime();
        //赋值
        MsgModel msgModel = new MsgModel();
        msgModel.model_code = msgModelAdd.model_code;
        msgModel.model_name = msgModelAdd.model_name;
        msgModel.model_content = msgModelAdd.model_content;
        msgModel.created_user = msgModelAdd.created_user;
        msgModel.created_user_type = msgModelAdd.created_user_type;
        msgModel.status = msgModelAdd.status;
        msgModel.state = msgModelAdd.state;
        msgModel.created_at = date;
        msgModel.updated_at = date;
        //调用DAO
        //判断模板是否重复
        MsgModel msgModelRepeat = msgModelDAO.getMsgModelRepeat(msgModel);
        if (msgModelRepeat != null) {
            return json(BaseResponse.CODE_FAILURE, "模板已存在，请检查模板名称", response);
        }
        int add_res = msgModelDAO.addMsgModel(msgModel);
        if (add_res == 1) {
            return json(BaseResponse.CODE_SUCCESS, "模板创建成功，等待审核", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "模板创建失败", response);
        }
    }

    /**
     * 站内信模板列表
     * todo 还没写，暂时不知道分页怎么写
     * @param page 当前页码 ，可不传，默认为1
     * @param last_id 上一页最大id ，可不传，默认为
     * @param limit 每页显示行数，可不传，默认为
     * @param model_status 模板状态（审核通过0/未通过1/已删除2）
     * @return json
     * @access public
     */

    public String listMsgModel(ActionBean actionBean) {
        MsgModelBean.msgModelList modelList = JsonKit.json2Bean(actionBean.body, MsgModelBean.msgModelList.class);
        //获取传进来的参数
//        MsgModelBean.msgModelInfo request = requst2Bean(modellist.model_status, MsgModelBean.msgModelInfo.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (modelList == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        //调用DAO
        MsgModelList msgModelList = new MsgModelList();
        MsgModel msgModel = new MsgModel();
        msgModelList.page = setPage(modelList.last_id, modelList.page_num, modelList.limit);
        msgModel.status = modelList.model_status;
        msgModelList.msgModel = msgModel;
        List<MsgModel> msgModels = msgModelDAO.getMsgModelList(msgModelList);
        response.data = msgModels;
        if (msgModels != null) {
            return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
    }

    /**
     * 站内信模板详情
     *
     * @param model_code 模板代码
     * @return json
     * @access public
     */
    public String infoMsgModel(String body) {
        MsgModelBean.msgModelInfo msgModelInfo = JsonKit.json2Bean(body, MsgModelBean.msgModelInfo.class);
        //获取传进来的参数
        MsgModelBean.msgModelInfo request = requst2Bean(msgModelInfo.model_code, MsgModelBean.msgModelInfo.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (msgModelInfo == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        //调用DAO
        MsgModel msgModel = msgModelDAO.getMsgModelInfo(msgModelInfo.model_code);
        response.data = msgModel;
        if (msgModel != null) {
            return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
    }

    /**
     * 站内信模板操作（审核、删除）
     *
     * @param model_code   模板代码
     * @param operate_code 操作代码（审核通过1，删除2）
     * @param user_id      操作人id
     * @param user_type    操作人类型（只有业管可以审核和删除）
     * @return json
     * @access public
     */
    public String updateMsgModel(String body) {
        MsgModelBean.msgModelUpdate msgModelUpdate = JsonKit.json2Bean(body, MsgModelBean.msgModelUpdate.class);
        //获取传进来的参数
        MsgModelBean.msgModelInfo request = requst2Bean(msgModelUpdate.model_code, MsgModelBean.msgModelInfo.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (msgModelUpdate == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        if (msgModelUpdate.user_type != 1) {//只有管理员才能操作站内信模板
            return json(BaseResponse.CODE_FAILURE, "操作失败，没有权限", response);
        }
        //赋值
        MsgModelUpdate modelUpdate = new MsgModelUpdate();
        modelUpdate.model_code = msgModelUpdate.model_code;
        modelUpdate.status = msgModelUpdate.status;
        //调用DAO
        int updateRes = msgModelDAO.updateMsgModel(modelUpdate);
        if (updateRes != 0) {
            return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
    }
}
