package com.inschos.message.access.http.controller.action;


import com.inschos.message.access.http.controller.bean.*;
import com.inschos.message.access.rpc.bean.AccountBean;
import com.inschos.message.access.rpc.bean.AgentJobBean;
import com.inschos.message.access.rpc.client.AccountClient;
import com.inschos.message.access.rpc.client.AgentJobClient;
import com.inschos.message.access.rpc.client.ChannelClient;
import com.inschos.message.assist.kit.L;
import com.inschos.message.assist.kit.ListKit;
import com.inschos.message.assist.kit.TimeKit;
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

    @Autowired
    private ChannelClient channelClient;
    @Autowired
    private AgentJobClient agentJobClient;
    @Autowired
    private AccountClient accountClient;

    /**
     * 发送消息
     *
     * @paramss title|标题
     * @paramss content|内容
     * @paramss attachment|附件:上传附件的URL,可为空
     * @paramss type|消息                            类型:系统通知1/保单助手2/理赔进度3/最新任务4/客户消息5/活动消息6/顾问消息7/
     * @paramss fromId|发件人ID
     * @paramss fromType|发件人类型:用户类型:个人用户           1/企业用户 2/代理人 3/业管用户 4
     * @paramss toId|收件人id
     * @paramss toType|收件人类型:用户类型:个人用户             1/企业用户 2/代理人 3/业管用户 4
     * @paramss status|读取状态:标识消息                   是否已被读取,未读0/已读1.避免重复向收件箱表插入数据,默认为0
     * @paramss sendTime|发送时间:默认为空。需要延时发送的，发送时间不为空
     * @paramss parentId|消息父级id
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
    public String addMessage(ActionBean actionBean){
        MsgIndexBean request = JsonKit.json2Bean(actionBean.body, MsgIndexBean.class);
        BaseResponse response = new BaseResponse();
            if (request == null) {
                return json(BaseResponse.CODE_FAILURE, "参数解析失败", response);
            }
            if (request.title== null || request.content== null) {
                return json(BaseResponse.CODE_FAILURE, "标题和内容不能为空", response);
            }
            if (request.toUser == null || request.toUser.size() == 0) {
                return json(BaseResponse.CODE_FAILURE, "发送对象不能为空", response);
            }
        //消息配置
        MsgStatus msgStatus = new MsgStatus();
        //TODO 权限判断 个人1/企业2/代理人3/业管4
        //只有个人用户不能发送多条和系统消息
        if (request.toUser.size() > 1 && request.fromType == msgStatus.USER_PERSON) {
            return json(BaseResponse.CODE_FAILURE, "您没有权限执行这项操作", response);
        }
        long date = new Date().getTime();
        if(actionBean.managerUuid==null){
            actionBean.managerUuid = "-1";
        }
        MsgSys msgSys = new MsgSys();
        msgSys.manager_uuid = actionBean.managerUuid;
        msgSys.account_uuid = actionBean.accountUuid;
        msgSys.business_id = request.businessId;
        msgSys.title = request.title;
        msgSys.content = request.content;
        msgSys.type = request.type;
        if (request.attachment == null) {
            request.attachment = "";
        }
        msgSys.attachment = request.attachment;
        msgSys.send_time = request.sendTime;
        msgSys.from_id =  Long.valueOf(actionBean.userId);
        msgSys.from_type = actionBean.userType;
        msgSys.created_at = date;
        msgSys.updated_at = date;
        AddMsgRecord addMsgRecord = new AddMsgRecord();
        addMsgRecord.toUser = request.toUser;
        List<AgentJobBean> agentJobBeans = findChannelUser(addMsgRecord, actionBean.sysId, actionBean.managerUuid);
        logger.info(agentJobBeans.size());
        if(agentJobBeans.size()==0){
            return json(BaseResponse.CODE_FAILURE, "消息接收人为空,发送失败", response);
        }
        int send_result = msgIndexDAO.addMessage(msgSys);

        if(send_result>0){
            addMsgRecord.messageId = msgSys.id;
            String add_record =  addMsgRecord(addMsgRecord,actionBean.sysId,actionBean.managerUuid,agentJobBeans);
            if(add_record!=null){
                return json(BaseResponse.CODE_SUCCESS, "发送成功", response);
            }else{
                return json(BaseResponse.CODE_FAILURE, "发送失败", response);
            }
        }else{
            return json(BaseResponse.CODE_FAILURE, "发送失败", response);
        }
    }

    /**
     * 判断当前渠道项目有没有用户
     * @param addMsgRecord
     * @param sysId
     * @param managerUuid
     * @return
     */
    public List<AgentJobBean> findChannelUser(AddMsgRecord addMsgRecord, int sysId, String managerUuid){
        List<AgentJobBean> agentJobBeans = new ArrayList<>();
        for (AddMsgToBean addMsgToBean : addMsgRecord.toUser) {
            MsgRecord msgRecord = new MsgRecord();
            if(addMsgToBean.toId==0||addMsgToBean.toType==0||addMsgToBean.toType==5){//没有代理人，只有渠道id
                List<String> childrenId = channelClient.getChildrenId(String.valueOf(addMsgToBean.channelId), true);
                AgentJobBean searchAgents = new AgentJobBean();
                searchAgents.channelIdList = childrenId;
                searchAgents.manager_uuid = managerUuid;
                searchAgents.search_cur_time = TimeKit.curTimeMillis2Str();
                List<AgentJobBean> agents = agentJobClient.getAgentsByChannels(searchAgents);
                if(agents!=null){
                    agentJobBeans.addAll(agents);
//                    personIds.addAll(ListKit.toColumnList(agents,v->v.person_id));
                }
                logger.info(5);
            }else if(addMsgToBean.toType==4){
                AgentJobBean agentJobBean = agentJobClient.getAgentById(addMsgToBean.toId);
                if(agentJobBean!=null){
                    agentJobBeans.add(agentJobBean);
                }
                logger.info(4);
            }
        }
//        if(personIds.size()>0){
//            ListKit.toUnique(personIds);
//            ListKit.toRemoveO(personIds,0l);
//        }
        return agentJobBeans;
    }

    /**
     * 处理没有收件人的发送操作
     * @param addMsgRecord
     * @param sysId
     * @param managerUuid
     * @return
     */
    public String addMsgRecord(AddMsgRecord addMsgRecord,int sysId,String managerUuid,List<AgentJobBean> agentJobBeans){
        BaseResponse response = new BaseResponse();
        long date = new Date().getTime();
        List<String> accountUuids = new ArrayList<>();
        for (AgentJobBean agentJobBean : agentJobBeans) {
            MsgRecord msgRecord = new MsgRecord();
            msgRecord.msg_id = addMsgRecord.messageId;
            msgRecord.rec_id = agentJobBean.id;
            msgRecord.type = 4;
            msgRecord.state = 1;
            msgRecord.status = 1;
            msgRecord.created_at = date;
            msgRecord.updated_at = date;
            RepeatCount msgRecordRepeat = msgIndexDAO.findAddMsgRecordRepeat(msgRecord);//发件记录表
            if(msgRecordRepeat.count==0){
                int addMsgRec = msgIndexDAO.addMessageRecord(msgRecord);//发件记录表
            }
            AccountBean accountBean = accountClient.findByUser(sysId, AccountBean.USER_TYPE_AGENT, String.valueOf(agentJobBean.person_id));
            if(accountBean!=null){
                accountUuids.add(accountBean.accountUuid);
            }
        }
        L.log.debug("accountUuid  size {}",accountUuids.size());
        for (String accountUuid : accountUuids) {
            L.log.debug("accountUuid {}",accountUuid);
            MsgToRecord msgToRecord = new MsgToRecord();
            msgToRecord.account_uuid = accountUuid;
            msgToRecord.manager_uuid = managerUuid;
            msgToRecord.msg_id = addMsgRecord.messageId;
            msgToRecord.status = 1;
            msgToRecord.state = 1;
            msgToRecord.created_at = date;
            msgToRecord.updated_at = date;
            RepeatCount repeatCountToRecord = msgIndexDAO.findMessageToRecordRepeat(msgToRecord);//用户记录表
            if(repeatCountToRecord.count==0){
                int addToRec = msgIndexDAO.addMessageToRecord(msgToRecord);//用户记录表
            }
        }
        return json(BaseResponse.CODE_SUCCESS, "添加发送记录成功", response);
    }

    /**
     * 操作消息 （收件箱 读取和删除）
     *
     * @paramss messageId   消息 id
     * @paramss operateId   操作代码:默认为1（删除/已读），2（还原/未读）
     * @paramss operateType 操作类型:read 更改读取状态，del 更改删除状态
     * @return json
     * @access public
     */
    public String updateMsgRec(ActionBean actionBean) {
        MsgInboxBean.MsgUpdateRequest request = JsonKit.json2Bean(actionBean.body, MsgInboxBean.MsgUpdateRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "参数解析失败", response);
        }
        if(request.operateAll==0){//批量操作
            if (request.messageIds == null || request.messageIds.size() == 0) {
                return json(BaseResponse.CODE_FAILURE, "请选择您要操作的消息", response);
            }
            List<MsgUpdateResBean> msgUpdateResList = new ArrayList<>();
            MsgUpdateResBean msgUpdateRes = new MsgUpdateResBean();
            for (MsgUpdateBean messageId : request.messageIds) {
                MsgUpdate msgUpdate = new MsgUpdate();
                msgUpdate.msg_id = messageId.messageId;
                msgUpdate.operate_id = request.operateId;
                msgUpdate.type = request.messageType;
                msgUpdate.manager_uuid = actionBean.managerUuid;
                msgUpdate.account_uuid = actionBean.accountUuid;
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
                }
            }
            response.data = msgUpdateResList;
        }else if(request.operateAll==1){//处理全部
            MsgUpdate msgUpdate = new MsgUpdate();
            msgUpdate.operate_id = request.operateId;
            msgUpdate.manager_uuid = actionBean.managerUuid;
            msgUpdate.account_uuid = actionBean.accountUuid;
            msgUpdate.type = request.messageType;
            switch (request.operateType){
                case "read":
                    msgUpdate.operate_type = "sys_status";
                    int updateStatusRes = msgInboxDAO.updateAllMsgRecStatus(msgUpdate);
                    response.data = updateStatusRes;
                    break;
                case "del":
                    msgUpdate.operate_type = "state";
                    int updateStateRes = msgInboxDAO.updateAllMsgRecState(msgUpdate);
                    response.data = updateStateRes;
                    break;
            }
        }
        if(response.data!=null&&response.data!="0"){
            return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
        }else{
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
    }

}
