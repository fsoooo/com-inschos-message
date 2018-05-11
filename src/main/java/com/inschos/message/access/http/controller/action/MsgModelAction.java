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
     * 添加站内信模板
     *
     * @param model_name        模板名称（不能一样）
     * @param model_content     模板内容
     * @param created_user      创建者姓名
     * @param created_user_type 创建者类型
     * @return json
     * @access public
     */
    public String addMsgModel(ActionBean actionBean) {
        MsgModelBean.addRequest request = JsonKit.json2Bean(actionBean.body, MsgModelBean.addRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        //获取当前时间戳(毫秒值)
        long date = new Date().getTime();
        String code  = getStringRandom(6);
        //赋值
        MsgModel msgModel = new MsgModel();
        msgModel.model_code = code;
        msgModel.model_name = request.model_name;
        msgModel.model_content = request.model_content;
        msgModel.created_user = request.created_user;
        msgModel.created_user_type = request.created_user_type;
        msgModel.status = request.status;
        msgModel.state = request.state;
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
        MsgModelBean.listRequest request = JsonKit.json2Bean(actionBean.body, MsgModelBean.listRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        //调用DAO
        MsgModelList msgModelList = new MsgModelList();
        msgModelList.page = setPage(request.last_id, request.page_num, request.limit);
        MsgModel msgModel = new MsgModel();
        msgModel.status = request.model_status;
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
    public String infoMsgModel(ActionBean actionBean) {
        MsgModelBean.infoRequest request = JsonKit.json2Bean(actionBean.body, MsgModelBean.infoRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        //调用DAO
        MsgModel msgModel = msgModelDAO.getMsgModelInfo(request.model_code);
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
    public String updateMsgModel(ActionBean actionBean) {
        MsgModelBean.updateRequest request = JsonKit.json2Bean(actionBean.body, MsgModelBean.updateRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        if (request.user_type != 1) {//只有管理员才能操作站内信模板
            return json(BaseResponse.CODE_FAILURE, "操作失败，没有权限", response);
        }
        //赋值
        MsgModelUpdate modelUpdate = new MsgModelUpdate();
        modelUpdate.model_code = request.model_code;
        modelUpdate.status = request.status;
        //调用DAO
        int updateRes = msgModelDAO.updateMsgModel(modelUpdate);
        if (updateRes != 0) {
            return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
    }

    /**
     * 生成随机数字和字母
     * @param length
     * @return
     */
    public String getStringRandom(int length) {
        String val = "";
        Random random = new Random();
        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}
