package com.inschos.message.access.http.controller.action;


import com.inschos.message.access.http.controller.bean.*;
import com.inschos.message.data.dao.MsgInboxDAO;
import com.inschos.message.data.dao.MsgIndexDAO;
import com.inschos.message.assist.kit.JsonKit;
import com.inschos.message.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class MsgIndexAction extends BaseAction {
    private static final Logger logger = Logger.getLogger(MsgIndexAction.class);
    @Autowired
    private MsgIndexDAO msgIndexDAO;
    @Autowired
    private MsgInboxDAO msgInboxDAO;

    /**
     * 发送消息
     *
     * @params title|标题
     * @params content|内容
     * @params attachment|附件:上传附件的URL,可为空
     * @params type|消息                            类型:系统通知1/保单助手2/理赔进度3/最新任务4/客户消息5/活动消息6/顾问消息7/
     * @params fromId|发件人ID
     * @params fromType|发件人类型:用户类型:个人用户           1/企业用户 2/代理人 3/业管用户 4
     * @params toId|收件人id
     * @params toType|收件人类型:用户类型:个人用户             1/企业用户 2/代理人 3/业管用户 4
     * @params status|读取状态:标识消息                   是否已被读取,未读0/已读1.避免重复向收件箱表插入数据,默认为0
     * @params sendTime|发送时间:默认为空。需要延时发送的，发送时间不为空
     * @params parentId|消息父级id
     * @return json
     * @access public
     * <p>
     * TODO 消息 要素判断-05.14
     * TODO 群发消息 判断-05.15 业管可以发送所有类型的消息，代理人可以给自己的客户群发消息，企业用户可以给自己的员工群发消息，个人用户只能发送私信
     * TODO 延时发送判断-05.15  如果要延时发送消息 ，定时触发机制？？？
     * TODO 定时任务,用来处理定时发送的消息。Spring 自带的定时任务执行@Scheduled注解，可以定时的、周期性的执行一些任务。
     * TODO 上传文件-邮件附件-05.15 前端请求->请求文件服务->上传文件,返回key;前端消息要素(key)->发送消息接口->发送消息
     * TODO 判断是否重复插入？
     * TODO 附件格式，只存储，不操作。(端上获取key,传参,存储)
     * TODO 个人用户可以跟代理人（顾问）联系。待确定用户之间的联系方式
     */
    public String addMessage(ActionBean actionBean) {
        MsgIndexBean request = JsonKit.json2Bean(actionBean.body, MsgIndexBean.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        if (request.title.isEmpty() || request.content.isEmpty()) {
            return json(BaseResponse.CODE_FAILURE, "title or content is empty", response);
        }
        if(request.type == 0){
            return json(BaseResponse.CODE_FAILURE, "type is empty", response);
        }
        if (request.fromId == 0 || request.fromType == 0) {
            return json(BaseResponse.CODE_FAILURE, "from_id or from_type is empty", response);
        }
        if (request.toUser == null || request.toUser.size() == 0) {
            return json(BaseResponse.CODE_FAILURE, "to_user is empty", response);
        }
        //消息配置
        MsgStatus msgStatus = new MsgStatus();
        //TODO 权限判断 个人1/企业2/代理人3/业管4
        if (request.toUser.size() > 1 && request.fromType == msgStatus.USER_PERSON) {//只有个人用户不能发送多条和系统消息
            return json(BaseResponse.CODE_FAILURE, "no permission", response);
        }
        //TODO 系统消息toId和toType都等于-1，多发，和私信
        //TODO 发件人发的消息，类型有限制:个人-顾问消息；代理人，企业，业管
        //赋值
        MsgSys msgSys = new MsgSys();
        List<MsgSendResBean> msgSendResList = new ArrayList<>();
        MsgSendResBean msgSendRes = new MsgSendResBean();
        //获取当前时间戳(毫秒值)
        long date = new Date().getTime();
        for (MsgToBean msgToBean : request.toUser) {
            if (msgToBean.toId == msgStatus.MSG_SYS_KEY || msgToBean.toType == msgStatus.MSG_SYS_KEY) {
                if (request.fromType != msgStatus.USER_MANAGER) {//只有业管系统消息
                    return json(BaseResponse.CODE_FAILURE, "no permission", response);
                }
            }
            msgSys.manager_uuid = actionBean.managerUuid;
            msgSys.account_uuid = actionBean.accountUuid;
            msgSys.title = request.title;
            msgSys.content = request.content;
            msgSys.type = request.type;
            if (request.attachment == null) {
                request.attachment = "";
            }
            msgSys.attachment = request.attachment;
            msgSys.send_time = request.sendTime;
            msgSys.from_id = request.fromId;
            msgSys.from_type = request.fromType;
            msgSys.to_id = msgToBean.toId;
            msgSys.to_type = msgToBean.toType;
            msgSys.channel_id = msgToBean.channelId;
            msgSys.parent_id = request.parentId;
            msgSys.created_at = date;
            msgSys.updated_at = date;
            //调用DAO
            msgSendRes.toId = msgToBean.toId;
            msgSendRes.toType = msgToBean.toType;
            msgSendRes.channelId = msgToBean.channelId;

            int send_result = msgIndexDAO.addMsgSys(msgSys);
            if (send_result == 1) {
                msgSendRes.sendRes = "发送成功";
            } else {
                msgSendRes.sendRes = "发送失败";
            }
            msgSendResList.add(msgSendRes);
        }
        response.data = msgSendResList;
        return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
    }

    /**
     * 操作消息 （收件箱 读取和删除）
     *
     * @params messageId   消息 id
     * @params operateId   操作代码:默认为1（删除/已读），2（还原/未读）
     * @params operateType 操作类型:read 更改读取状态，del 更改删除状态
     * @return json
     * @access public
     */
    public String updateMsgRec(ActionBean actionBean) {
        MsgInboxBean.MsgUpdateRequest request = JsonKit.json2Bean(actionBean.body, MsgInboxBean.MsgUpdateRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        if(request.userType!=4){
            return json(BaseResponse.CODE_FAILURE, "no permission", response);
        }
        if (request.messageIds == null || request.messageIds.size() == 0) {
            return json(BaseResponse.CODE_FAILURE, "messageIds is empty", response);
        }
        List<MsgUpdateResBean> msgUpdateResList = new ArrayList<>();
        MsgUpdateResBean msgUpdateRes = new MsgUpdateResBean();
        for (MsgUpdateBean messageId : request.messageIds) {
            MsgUpdate msgUpdate = new MsgUpdate();
            msgUpdate.msg_id = messageId.messageId;
            msgUpdate.operate_id = request.operateId;
            switch (request.operateType){
                case "read":
                    msgUpdate.operate_type = "sys_status";
                    int updateStatusRes = msgInboxDAO.updateMsgRecStatus(msgUpdate);
                    if(updateStatusRes==1){
                        msgUpdateRes.updateRes = "更新成功";
                    }else{
                        msgUpdateRes.updateRes = "更新失败";
                    }
                    msgUpdateRes.messageId = messageId.messageId;
                    msgUpdateResList.add(msgUpdateRes);
                    break;
                case "del":
                    msgUpdate.operate_type = "state";
                    int updateStateRes = msgInboxDAO.updateMsgRecState(msgUpdate);
                    if(updateStateRes==1){
                        msgUpdateRes.updateRes = "更新成功";
                    }else{
                        msgUpdateRes.updateRes = "更新失败";
                    }
                    msgUpdateRes.messageId = messageId.messageId;
                    msgUpdateResList.add(msgUpdateRes);
                    break;
                default:
                    msgUpdateRes.messageId = 0;
                    msgUpdateRes.updateRes = "";
                    msgUpdateResList.add(msgUpdateRes);
                    break;
            }
        }
        response.data = msgUpdateResList;
        return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
    }
}
