package com.inschos.message.access.http.controller.action;

import com.inschos.message.access.http.controller.bean.BaseRequest;
import com.inschos.message.access.http.controller.bean.BaseResponse;
import com.inschos.message.access.http.controller.bean.MsgInboxBean;
import com.inschos.message.access.http.controller.bean.MsgModelBean;
import com.inschos.message.data.dao.MsgInboxDAO;
import com.inschos.message.assist.kit.JsonKit;
import com.inschos.message.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MsgInboxAction extends BaseAction {
    private static final Logger logger = Logger.getLogger(MsgInboxAction.class);
    @Autowired
    private MsgInboxDAO msgInboxDAO;
    private Page page;

    /**
     * 站内信收件箱列表
     *
     * @param user_id        用户id
     * @param user_type      用户类型:个人用户 3/代理人 2/企业用户 1/管理员
     * @param Page           分页页码 （非必传，默认为1）
     * @param message_status 站内信状态:未读 0/已读 1/全部 2/删除 3 （非必传，默认为0）
     * @return json
     * <p>
     * 业管可以查看所有人的站内信
     * 站内信列表组成：站内信系统表里收件人id为0的（系统消息）+ 站内信系统表里收件人id为user_id的（订阅消息、私信）
     * 匹配站内信系统表和站内信收件箱表，向用户收件箱里插入相应的数据，并修改站内信系统表的状态
     * todo 只要用户接收站内信，系统表就默认已经读取了，不在插入
     * @access public
     */
    public String getMsgRecList(String body) {
        MsgInboxBean.msgInboxList inboxList = JsonKit.json2Bean(body, MsgInboxBean.msgInboxList.class);
        //获取传进来的参数
        //MsgInboxBean.msgInboxList request = requst2Bean(inboxList.message_status, MsgModelBean.msgModelAdd.class);
        BaseResponse response = new BaseResponse();
        //判空
        if(inboxList==null){
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        //赋值
//        MsgRec msgRec = new MsgRec();
//        msgRec.page. = inboxList.pageBean.pageSize;
//        msgRec.page = inboxList.pageBean.lastId;
//        msgRec.page = inboxList.pageBean.pageNum;
//        msgRec.page = inboxList.pageBean.pageTotal;
//        msgRec.page = inboxList.pageBean.total;
//        msgRec.page = inboxList.pageBean.listSize;

//        msgRec.user_id = inboxList.user_id;
//        msgRec.user_type = inboxList.user_type;
//        msgRec.sys_status = inboxList.message_status;
//        //调用DAO
//        //判断模板是否重复
//        MsgModel msgModelRepeat = msgModelDAO.getMsgModelRepeat(msgModel);
//        if(msgModelRepeat!=null){
//            return json(BaseResponse.CODE_FAILURE, "模板已存在，请检查模板名称", response);
//        }
//        int add_res = msgModelDAO.addMsgModel(msgModel);
//        if(add_res==1){
//            return json(BaseResponse.CODE_SUCCESS, "模板创建成功，等待审核", response);
//        }else{
            return json(BaseResponse.CODE_FAILURE, "模板创建失败", response);
//        }
    }


    /**
     * 站内信发件箱列表
     *
     * @param user_id   用户id
     * @param user_type 用户类型:个人用户 3/代理人 2/企业用户 1/管理员 0
     * @param Page      分页页码 （非必传，默认为1）
     * @return json
     * @access public
     */
    public String getMsgSysList(String body) {
        MsgSys msgSys = JsonKit.json2Bean(body, MsgSys.class);
        BaseRequest request = requst2Bean(msgSys.type, BaseRequest.class);
        BaseResponse response = new BaseResponse();
        return json(BaseResponse.CODE_FAILURE, "业务完善中", response);
        //return msgInboxDAO.getMsgSysList(msgSys);
    }

    /**
     * 站内信详情
     *
     * @param message_id 站内信id
     * @return json
     * @access public
     */
    public String getMsgInfo(String body) {
        MsgSys msgSys = JsonKit.json2Bean(body, MsgSys.class);
        BaseRequest request = requst2Bean(msgSys.type, BaseRequest.class);
        BaseResponse response = new BaseResponse();
        return json(BaseResponse.CODE_FAILURE, "业务完善中", response);
        //return msgInboxDAO.getMsgInfo(msg_id);
    }

    /**
     * 操作站内信（收件箱 读取和删除）
     *
     * @param message_id   站内信id
     * @param operate_id   操作代码:默认为1（删除/已读），2（还原/未读）
     * @param operate_type 操作类型:read 更改读取状态，del 更改删除状态
     * @return json
     * @access public
     */
    public String updateMsgRec(String body) {
        MsgUpdate msgUpdate = JsonKit.json2Bean(body, MsgUpdate.class);
        BaseRequest request = requst2Bean(msgUpdate.update_data, BaseRequest.class);
        BaseResponse response = new BaseResponse();
        return json(BaseResponse.CODE_FAILURE, "业务完善中", response);
        //return msgInboxDAO.updateMsgRec(msgUpdate);
    }
}
