package com.inschos.message.access.http.controller.action;


import com.inschos.message.access.http.controller.bean.*;
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

    /**
     * 发送消息
     *
     * @param title|标题
     * @param content|内容
     * @param attachment|附件:上传附件的URL,可为空
     * @param type|消息 类型:系统通知1/保单助手2/理赔进度3/最新任务4/客户消息5/活动消息6/顾问消息7/
     * @param fromId|发件人ID
     * @param fromType|发件人类型:个人用户1/企业用户2/业管用户等3
     * @param toId|收件人id
     * @param toType|收件人类型:个人用户1/企业用户2/业管用户等3
     * @param status|读取状态:标识消息 是否已被读取,未读0/已读1.避免重复向收件箱表插入数据,默认为0
     * @param sendTime|发送时间:默认为空。需要延时发送的，发送时间不为空
     * @param parentId|消息父级id
     *
     * @return json
     * @access public
     *
     * TODO 消息 要素判断-05.14
     * TODO 群发消息 判断-05.15 业管可以发送所有类型的消息，代理人可以给自己的客户群发消息，企业用户可以给自己的员工群发消息，个人用户只能发送私信
     * TODO 延时发送判断-05.15  如果要延时发送消息 ，定时触发机制？？？
     * TODO 定时任务,用来处理定时发送的消息。Spring 自带的定时任务执行@Scheduled注解，可以定时的、周期性的执行一些任务。
     * TODO 上传文件-邮件附件-05.15 前端请求->请求文件服务->上传文件,返回key;前端消息要素(key)->发送消息接口->发送消息
     * TODO 判断是否重复插入？
     * TODO 附件格式，只存储，不操作。(端上获取key,传参,存储)
     * TODO 个人用户可以跟代理人（顾问）联系。待确定用户之间的联系方式
     *
     */
    public String addMessage(ActionBean actionBean) {
        MsgIndexBean request = JsonKit.json2Bean(actionBean.body, MsgIndexBean.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        if(request.title.isEmpty()||request.content.isEmpty()){
            return json(BaseResponse.CODE_FAILURE, "title or content is empty", response);
        }
        if(request.fromId==0||request.fromType==0){
            return json(BaseResponse.CODE_FAILURE, "from_id or from_type is empty", response);
        }
        if (request.toUser == null||request.toUser.size()==0) {
            return json(BaseResponse.CODE_FAILURE, "to_user is empty", response);
        }
        //消息配置
        MsgStatus msgStatus = new MsgStatus();
        //TODO 权限判断 个人1/企业2/代理人3/业管4
        if(request.toUser.size()>1&&request.fromType==msgStatus.USER_PERSON){//只有个人用户不能发送多条和系统消息
            return json(BaseResponse.CODE_FAILURE, "no permission", response);
        }
        //TODO 系统消息toId和toType都等于-1，多发，和私信
        //TODO 发件人发的消息，类型有限制:个人-顾问消息；代理人，企业，业管
        //赋值
        MsgSys msgSys = new MsgSys();
        List<MsgSendRes> msgSendResList = new ArrayList<>();
        MsgSendRes msgSendRes = new MsgSendRes();
        //获取当前时间戳(毫秒值)
        long date = new Date().getTime();
        for (MsgToBean msgToBean : request.toUser) {
            if(msgToBean.toId==msgStatus.MSG_SYS_KEY||msgToBean.toType==msgStatus.MSG_SYS_KEY){
                if(request.fromType!=msgStatus.USER_MANAGER){//只有业管系统消息
                    return json(BaseResponse.CODE_FAILURE, "no permission", response);
                }
                //TODO 如果发送的是系统消息,只需要插一次
                msgSys.title = request.title;
                msgSys.content = request.content;
                msgSys.type = request.type;
                if(request.attachment.isEmpty()){
                    request.attachment = "";
                }
                msgSys.attachment = request.attachment;
                msgSys.send_time = request.sendTime;
                msgSys.from_id = request.fromId;
                msgSys.from_type = request.fromType;
                msgSys.to_id = msgToBean.toId;
                msgSys.to_type = msgToBean.toType;
                msgSys.parent_id = request.parentId;
                msgSys.created_at = date;
                msgSys.updated_at = date;
                //调用DAO
                msgSendRes.to_id = msgToBean.toId;
                msgSendRes.to_type = msgToBean.toType;
                int send_result = msgIndexDAO.addMsgSys(msgSys);
                if(send_result==1){
                    msgSendRes.send_res = "发送成功";
                }else{
                    msgSendRes.send_res = "发送失败";
                }
                msgSendResList.add(msgSendRes);
                break;
            }else{
                msgSys.title = request.title;
                msgSys.content = request.content;
                msgSys.type = request.type;
                if(request.attachment.isEmpty()){
                    request.attachment = "";
                }
                msgSys.attachment = request.attachment;
                msgSys.send_time = request.sendTime;
                msgSys.from_id = request.fromId;
                msgSys.from_type = request.fromType;
                msgSys.to_id = msgToBean.toId;
                msgSys.to_type = msgToBean.toType;
                msgSys.parent_id = request.parentId;
                msgSys.created_at = date;
                msgSys.updated_at = date;
                //调用DAO
                msgSendRes.to_id = msgToBean.toId;
                msgSendRes.to_type = msgToBean.toType;
                int send_result = msgIndexDAO.addMsgSys(msgSys);
                if(send_result==1){
                    msgSendRes.send_res = "发送成功";
                }else{
                    msgSendRes.send_res = "发送失败";
                }
                msgSendResList.add(msgSendRes);
            }
        }
        response.data = msgSendResList;
        return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
    }
}
