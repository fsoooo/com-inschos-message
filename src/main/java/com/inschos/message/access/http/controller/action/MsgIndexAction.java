package com.inschos.message.access.http.controller.action;


import com.inschos.message.access.http.controller.bean.BaseRequest;
import com.inschos.message.access.http.controller.bean.BaseResponse;
import com.inschos.message.data.dao.MsgIndexDAO;
import com.inschos.message.kit.JsonKit;
import com.inschos.message.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class MsgIndexAction extends BaseAction{
    @Autowired
    private MsgIndexDAO msgIndexDAO;
    private static final Logger logger = Logger.getLogger(MsgIndexAction.class);

    /**
     * 发送站内信
     * @access public
     * @param $from|int  发件人id
     * @param $from_type|string 发件人类型:个人用户/企业用户/管理员等
     * @param $to|array 收件人id
     * @param $to_type|sting 收件人类型;个人用户/企业用户/管理员等
     * @param $title|string 主题
     * @param $body|string 内容
     * @param $type|string 站内信类型
     * @param $file|resource 附件 可空
     * @param $send_time|string 发送时间 可空
     * @return json
     */
    public String addMsgSys(String body){
        MsgSys msgSys = JsonKit.json2Bean(body, MsgSys.class);
        if(msgSys!=null){
            BaseRequest request = requst2Bean(msgSys.type, BaseRequest.class);
            BaseResponse response = new BaseResponse();
            return json(BaseResponse.CODE_FAILURE, "业务完善中", response);
        }else{
            return "params is empty";
        }
    }

    /**
     * 站内信发送详情
     * @access private
     * @param $title|标题
     * @param $content|内容
     * @param $attachment|附件:上传附件的URL,可为空
     * @param $type|站内信类型:系统通知、保单消息、理赔消息，其他（站内信分类，可为空）
     * @param $from_id|发件人ID
     * @param $from_type|发件人类型:个人用户1/企业用户2/管理员等3
     * @param $to_id|收件人id
     * @param $to_type|收件人类型:个人用户1/企业用户2/管理员等3
     * @param $status|读取状态:标识站内信是否已被读取,未读0/已读1.避免重复向收件箱表插入数据,默认为0
     * @param $send_time|发送时间:默认为空。需要延时发送的，发送时间不为空
     * @return json
     */
    public String sendMsgInfo(MsgSend msgSend){
        if(msgSend!=null){
            BaseRequest request = requst2Bean(msgSend.send_time, BaseRequest.class);
            BaseResponse response = new BaseResponse();
            return json(BaseResponse.CODE_FAILURE, "业务完善中", response);
        }else{
            return "params is empty";
        }
    }

    /**
     * 附件上传
     * @access private
     * @param MsgAttachment|resource 文件资源
     * @return $attachment_path 文件上传URL
     */
    public String attachmentUpload(MsgAttachment msgAttachment){
        if(msgAttachment!=null){
            BaseRequest request = requst2Bean(msgAttachment.file, BaseRequest.class);
            BaseResponse response = new BaseResponse();
            return json(BaseResponse.CODE_FAILURE, "业务完善中", response);
        }else{
            return "params is empty";
        }
    }
}
