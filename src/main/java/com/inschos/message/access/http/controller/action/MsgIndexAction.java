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
     * 发送站内信
     *
     * @param from      发件人id
     * @param from_type 发件人类型:个人用户/企业用户/管理员等
     * @param to        收件人id
     * @param to_type   收件人类型;个人用户/企业用户/管理员等
     * @param title     主题
     * @param body      内容
     * @param type      站内信类型
     * @param file      附件 可空
     * @param send_time 发送时间 可空
     * @return json
     * @access public
     * TODO 站内信要素判断-05.14
     * TODO 群发站内信判断-05.14
     * TODO 延时发送判断-05.14
     * TODO 上传文件-邮件附件-05.14
     */
    public String addMessage(ActionBean actionBean) {
        MsgIndexBean request = JsonKit.json2Bean(actionBean.body, MsgIndexBean.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        if (request.to_user == null) {
            return json(BaseResponse.CODE_FAILURE, "to_user is empty", response);
        }
        //赋值
        MsgSys msgSys = new MsgSys();
        List<MsgSendRes> msgSendResList = new ArrayList<>();
        MsgSendRes msgSendRes = new MsgSendRes();
        //获取当前时间戳(毫秒值)
        long date = new Date().getTime();
        //TODO  多个收件人还没有写  遍历循环插入
        for (MsgToBean msgToBean : request.to_user) {
            msgSys.title = request.title;
            msgSys.content = request.content;
            msgSys.type = request.type;
            msgSys.attachment = request.attachment;
            msgSys.send_time = request.send_time;
            msgSys.from_id = request.from_id;
            msgSys.from_type = request.from_type;
            msgSys.to_id = msgToBean.to_id;
            msgSys.to_type = msgToBean.to_type;
            msgSys.created_at = date;
            msgSys.updated_at = date;
            //调用DAO
            msgSendRes.to_id = msgToBean.to_id;
            msgSendRes.to_type = msgToBean.to_type;
            int send_result = msgIndexDAO.addMsgSys(msgSys);
            if(send_result==1){
                msgSendRes.send_res = "发送成功";
            }else{
                msgSendRes.send_res = "发送失败";
            }
            msgSendResList.add(msgSendRes);
        }
        response.data = msgSendResList;
        return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
    }

    /**
     * 站内信发送详情
     *
     * @param title|标题
     * @param content|内容
     * @param attachment|附件:上传附件的URL,可为空
     * @param type|站内信类型:系统通知、保单消息、理赔消息，其他（站内信分类，可为空）
     * @param from_id|发件人ID
     * @param from_type|发件人类型:个人用户1/企业用户2/管理员等3
     * @param to_id|收件人id
     * @param to_type|收件人类型:个人用户1/企业用户2/管理员等3
     * @param status|读取状态:标识站内信是否已被读取,未读0/已读1.避免重复向收件箱表插入数据,默认为0
     * @param send_time|发送时间:默认为空。需要延时发送的，发送时间不为空
     * @return json
     * @access private
     */
    public String sendMsgInfo(MsgSend msgSend) {
        if (msgSend != null) {
            BaseRequest request = requst2Bean(msgSend.send_time, BaseRequest.class);
            BaseResponse response = new BaseResponse();
            return json(BaseResponse.CODE_FAILURE, "业务完善中", response);
        } else {
            return "params is empty";
        }
    }

    /**
     * 附件上传
     *
     * @param MsgAttachment| 文件资源
     * @return attachment_path 文件上传URL
     * @access private
     */
    public String attachmentUpload(MsgAttachment msgAttachment) {
        if (msgAttachment != null) {
            BaseRequest request = requst2Bean(msgAttachment.file, BaseRequest.class);
            BaseResponse response = new BaseResponse();
            return json(BaseResponse.CODE_FAILURE, "业务完善中", response);
        } else {
            return "params is empty";
        }
    }
}
