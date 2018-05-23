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
import java.util.Random;

@Component
public class MsgModelAction extends BaseAction {
    private static final Logger logger = Logger.getLogger(MsgModelAction.class);
    @Autowired
    private MsgModelDAO msgModelDAO;
    private Page page;

    /**
     * 添加消息 模板
     *
     * @param modelName       模板名称（不能一样）
     * @param modelContent    模板内容
     * @param modelType       模板类型
     * @param createdUser     创建者姓名
     * @param createdUserType 创建者类型
     * @return json
     * @access public
     */
    public String addMsgModel(ActionBean actionBean) {
        MsgModelBean.AddRequest request = JsonKit.json2Bean(actionBean.body, MsgModelBean.AddRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        MsgStatus msgStatus = new MsgStatus();
        if (request.createdUserType != msgStatus.USER_MANAGER) {//TODO 只有业管才能添加模板 ??
            return json(BaseResponse.CODE_FAILURE, "no permission", response);
        }
        //获取当前时间戳(毫秒值)
        long date = new Date().getTime();
        String code = getStringRandom(6);
        //赋值
        MsgModel msgModel = new MsgModel();
        msgModel.model_code = code;
        msgModel.model_name = request.modelName;
        msgModel.model_content = request.modelContent;
        msgModel.model_type = request.modelType;
        msgModel.created_user = request.createdUser;
        msgModel.created_user_type = request.createdUserType;
        msgModel.status = request.status;
        msgModel.state = request.state;
        msgModel.created_at = date;
        msgModel.updated_at = date;
        //调用DAO
        //判断模板是否重复
        MsgModel msgModelRepeat = msgModelDAO.findMsgModelRepeat(msgModel);
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
     * 消息 模板列表
     *
     * @param pageNum     当前页码 ，可不传，默认为1
     * @param lastId      上一页最大id ，可不传，默认为
     * @param limit       每页显示行数，可不传，默认为
     * @param modelStatus 模板状态（审核通过0/未通过1/已删除2）
     * @param modelSype   模板类型
     * @return json
     * @access public
     */
    public String listMsgModel(ActionBean actionBean) {
        MsgModelBean.ListRequest request = JsonKit.json2Bean(actionBean.body, MsgModelBean.ListRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        //调用DAO
        MsgModelList msgModelList = new MsgModelList();
        msgModelList.page = setPage(request.lastId, request.pageNum, request.limit);
        MsgModel msgModel = new MsgModel();
        msgModel.status = request.modelStatus;
        msgModel.model_type = request.modelType;
        msgModelList.msgModel = msgModel;
        List<MsgModel> msgModels = msgModelDAO.findMsgModelList(msgModelList);
        response.data = msgModels;
        if (msgModels != null) {
            return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
    }

    /**
     * 消息 模板详情
     *
     * @param modelCode 模板代码
     * @return json
     * @access public
     */
    public String infoMsgModel(ActionBean actionBean) {
        MsgModelBean.InfoRequest request = JsonKit.json2Bean(actionBean.body, MsgModelBean.InfoRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        //调用DAO
        MsgModel msgModel = new MsgModel();
        msgModel.model_code = request.modelCode;
        MsgModel modelInfo = msgModelDAO.findMsgModelInfo(msgModel);
        response.data = modelInfo;
        if (modelInfo != null) {
            return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
    }

    /**
     * 消息 模板操作（审核、删除）
     *
     * @param modelCode 模板代码
     * @param status    模板状态（审核通过1，删除2）
     * @param modelType 模板类型
     * @param userId    操作人id
     * @param userType  操作人类型（只有业管可以审核和删除）
     * @return json
     * @access public
     */
    public String updateMsgModel(ActionBean actionBean) {
        MsgModelBean.UpdateRequest request = JsonKit.json2Bean(actionBean.body, MsgModelBean.UpdateRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        MsgStatus msgStatus = new MsgStatus();
        if (request.userType != msgStatus.USER_MANAGER) {//TODO 只有业管用户才能操作消息 模板？？
            return json(BaseResponse.CODE_FAILURE, "no permission", response);
        }
        //赋值
        MsgModelUpdate modelUpdate = new MsgModelUpdate();
        modelUpdate.model_code = request.modelCode;
        if (request.status == 0 && request.modelType == 0) {
            return json(BaseResponse.CODE_FAILURE, "no updated params", response);
        }
        if (request.status != 0 || request.modelType == 0) {
            modelUpdate.status = request.status;
            //调用DAO
            int updateRes = msgModelDAO.updateMsgModelStatus(modelUpdate);
            if (updateRes != 0) {
                return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
            } else {
                return json(BaseResponse.CODE_FAILURE, "操作失败", response);
            }
        }
        if (request.modelType != 0 || request.status == 0) {
            modelUpdate.status = request.status;
            modelUpdate.model_type = request.modelType;
            //调用DAO
            int updateRes = msgModelDAO.updateMsgModelType(modelUpdate);
            if (updateRes != 0) {
                return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
            } else {
                return json(BaseResponse.CODE_FAILURE, "操作失败", response);
            }
        }
        if (request.status != 0 && request.modelType != 0) {
            modelUpdate.status = request.status;
            modelUpdate.model_type = request.modelType;
            //调用DAO
            int updateRes = msgModelDAO.updateMsgModel(modelUpdate);
            if (updateRes != 0) {
                return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
            } else {
                return json(BaseResponse.CODE_FAILURE, "操作失败", response);
            }
        }else{
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
    }

    /**
     * 生成随机数字和字母
     *
     * @param length
     * @return
     */
    public String getStringRandom(int length) {
        String val = "";
        Random random = new Random();
        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}
