package com.inschos.message.access.http.controller.action;

import com.inschos.message.access.http.controller.bean.*;
import com.inschos.message.access.rpc.bean.AgentJobBean;
import com.inschos.message.access.rpc.bean.ChannelBean;
import com.inschos.message.access.rpc.client.AccountClient;
import com.inschos.message.access.rpc.client.AgentJobClient;
import com.inschos.message.access.rpc.client.ChannelClient;
import com.inschos.message.assist.kit.StringKit;
import com.inschos.message.assist.kit.TimeKit;
import com.inschos.message.data.dao.MsgInboxDAO;
import com.inschos.message.assist.kit.JsonKit;
import com.inschos.message.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class MsgInboxAction extends BaseAction {
    private static final Logger logger = Logger.getLogger(MsgInboxAction.class);
    @Autowired
    private MsgInboxDAO msgInboxDAO;

    @Autowired
    private ChannelClient channelClient;
    @Autowired
    private AgentJobClient agentJobClient;
    @Autowired
    private AccountClient accountClient;

    /**
     * 消息 收件箱列表(获取所有类型的列表)
     *
     * @paramss userId        用户id
     * @paramss userType      用户类型:个人用户 1/企业用户 2//代理人 3/业管用户4
     * @paramss messageStatus 消息 状态:未读1/已读2/全部3/删除4（非必传，默认为1）
     * @paramss pageNum       当前页码 ，可不传，默认为1
     * @paramss lastId        上一页最大id ，可不传，默认为
     * @paramss limit         每页显示行数，可不传，默认为
     * @return json
     * <p>
     * 业管可以查看所有人、所有类型的消息，返回按消息分类展示
     * 消息 列表组成：消息 系统表里收件人id为-1的（系统消息）+ 消息 系统表里收件人id为user_id的（订阅消息、私信）
     * 匹配消息 系统表和消息 收件箱表，向用户收件箱里插入相应的数据，并修改消息 系统表的状态
     * todo 只要用户接收消息 ，系统表就默认已经读取了，不在插入
     * @access public
     */
    public String findMsgRecList(ActionBean bean) {
        MsgInboxBean.InboxListRequest request = requst2Bean(bean.body, MsgInboxBean.InboxListRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        if(bean.managerUuid==null){
            bean.managerUuid = "-1";
        }
        //调用DAO
        MsgRec msgRec = new MsgRec();
        msgRec.page = setPage(request.lastId, request.pageNum, request.limit);
        msgRec.sys_status = request.messageStatus;
        msgRec.user_id = Long.valueOf(bean.userId);//Long.valueOf(bean.userId)
        msgRec.user_type = bean.userType;
        msgRec.manager_uuid = bean.managerUuid;
        msgRec.account_uuid = bean.accountUuid;
        MsgStatus msgStatus = new MsgStatus();
        //根据user_type判断不同用户可以查看消息 类型
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String insertRes = insertMsgRec(msgRec);
        List<MsgTypeLists> msgInboxList = msgInboxDAO.findMsgRecList(msgRec);
        if(msgInboxList==null){
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
        List<MsgInboxListBean> msgInboxLists = new ArrayList<>();
        long newLastId = 0;
        for (MsgTypeLists msgTypeLists : msgInboxList) {
            newLastId = msgTypeLists.mid;
            MsgInboxListBean msgInboxListBean = new MsgInboxListBean();
            msgInboxListBean.messageType = msgTypeLists.type;
            msgInboxListBean.messageTypeText = MsgStatus.getMsgType(msgTypeLists.type);
            msgInboxListBean.unReadCount = msgTypeLists.count;
            msgInboxListBean.unReadCountText = msgTypeLists.count + "条新消息";
            msgInboxListBean.time = msgTypeLists.time;
            msgInboxListBean.timeTxt = TimeKit.format("yyyy-MM-dd HH:mm:ss",msgTypeLists.time);

            msgInboxLists.add(msgInboxListBean);
        }
        MsgCount msgCount = msgInboxDAO.findMsgRecCount(msgRec);
        MsgListbean msgListbean = new MsgListbean();
        msgListbean.countNum = msgCount.count_num;
        msgListbean.msgLists = msgInboxLists;
        response.data = msgListbean;
        int size = msgInboxLists.size();
        if (StringKit.isInteger(request.pageNum)) {
            response.page = setPageBean(request.pageNum, request.pageSize, 0, size);
        } else if (StringKit.isInteger(request.lastId)) {
            response.page = setPageBean(newLastId, request.pageSize, 0, size);
        }
        if (response.data != null) {
            return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
    }

    /**
     * 消息 收件箱列表(获取所有类型的列表)
     *
     * @paramss userId        用户id
     * @paramss userType      用户类型:个人用户 1/企业用户 2//代理人 3/业管用户4
     * @paramss messageStatus 消息 状态:未读 1/已读 2/全部 3/（非必传，默认为1）
     * @paramss messageType   消息 类型:系统通知1/保单助手2/理赔进度3/最新任务4/客户消息5/活动消息6/顾问消息7/'
     * @paramss pageNum       当前页码 ，可不传，默认为1
     * @paramss lastId        上一页最大id ，可不传，默认为
     * @paramss limit         每页显示行数，可不传，默认为
     * @return json
     * 消息 列表组成：消息 系统表里收件人id为-1的（系统消息）+ 消息 系统表里收件人id为user_id的（订阅消息、私信）
     * 匹配消息 系统表和消息 收件箱表，向用户收件箱里插入相应的数据，并修改消息 系统表的状态
     * todo 只要用户接收消息 ，系统表就默认已经读取了，不在插入
     * @access public
     */
    public String findMsgResListByType(ActionBean bean) {
        MsgInboxBean.InboxListRequest request = JsonKit.json2Bean(bean.body, MsgInboxBean.InboxListRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        if (request.messageType == 0) {
            return json(BaseResponse.CODE_FAILURE, "messageType is empty", response);
        }
        //调用DAO
        MsgRec msgRec = new MsgRec();
        msgRec.page = setPage(request.lastId, request.pageNum, request.limit);
        if (request.messageStatus != 0) {
            msgRec.sys_status = request.messageStatus;
        }
        if(bean.managerUuid==null){
            bean.managerUuid = "-1";
        }
        msgRec.user_id = Integer.parseInt(bean.managerUuid);//Long.valueOf(bean.userId);
        msgRec.user_type = bean.userType;
        msgRec.type = request.messageType;
        msgRec.manager_uuid = bean.managerUuid;
        msgRec.account_uuid = bean.accountUuid;
        String insertRes = insertMsgRec(msgRec);
        List<MsgRec> msgRecList = msgInboxDAO.findMsgRecListByType(msgRec);
        if(msgRecList==null){
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
        List<MsgInboxListTypeBean> msgInboxListTypeBeans = new ArrayList<>();
        long newLastId = 0;
        for (MsgRec msgInboxRes : msgRecList) {
            MsgInboxListTypeBean msgInboxListType = new MsgInboxListTypeBean();
            msgInboxListType.id = msgInboxRes.id;
            newLastId = msgInboxRes.id;
            msgInboxListType.title = msgInboxRes.msgSys.title;
            List<String> list = new ArrayList<>();
            String str[] =  msgInboxRes.msgSys.content.split("\n");
            list = Arrays.asList(str);
            msgInboxListType.content = list;
            msgInboxListType.messageType = msgInboxRes.type;
            msgInboxListType.messageTypeText = MsgStatus.getMsgType(msgInboxRes.type);
            msgInboxListType.readFlag = msgInboxRes.sys_status;
            msgInboxListType.time = msgInboxRes.created_at;
            msgInboxListType.timeTxt = TimeKit.format("yyyy-MM-dd HH:mm:ss",msgInboxRes.created_at);
            newLastId = msgInboxRes.id;
            msgInboxListTypeBeans.add(msgInboxListType);
        }
        response.data = msgInboxListTypeBeans;
        int size = msgInboxListTypeBeans.size();
        if (StringKit.isInteger(request.pageNum)) {
            int total = msgInboxDAO.findMsgRecCountByType(msgRec);
            response.page = setPageBean(request.pageNum, request.pageSize, total, size);
        } else if (StringKit.isInteger(request.lastId)) {
            response.page = setPageBean(newLastId, request.pageSize, 0, size);
        }
        return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
    }

    /**
     * 消息 详情
     *
     * @paramss messageId 消息 id
     * @return json
     * @access public
     */
    public String findMsgInboxInfo(ActionBean actionBean) {
        MsgInboxBean.MsgInfoRequest request = JsonKit.json2Bean(actionBean.body, MsgInboxBean.MsgInfoRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        MsgRec msgRec = new MsgRec();
        MsgSys msgSys = new MsgSys();
        msgRec.id = request.messageId;
        if(actionBean.managerUuid==null){
            actionBean.managerUuid = "-1";
        }
        msgRec.user_id = Integer.parseInt(actionBean.managerUuid);
        msgRec.user_type = actionBean.userType;
        msgRec.manager_uuid = actionBean.managerUuid;
        msgRec.account_uuid = actionBean.accountUuid;
        MsgRec msgInfo = msgInboxDAO.findMsgInfo(msgRec);
        if(msgInfo==null){
            return json(BaseResponse.CODE_FAILURE, "not found msgInfo", response);
        }
        MsgInboxInfoBean msgInboxInfoBean = new MsgInboxInfoBean();
        msgInboxInfoBean.id = msgInfo.id;
        msgInboxInfoBean.title = msgInfo.msgSys.title;
        List<String> list = new ArrayList<>();
        String str[] =  msgInfo.msgSys.content.split("\n");
        list = Arrays.asList(str);
        msgInboxInfoBean.content = list;
        msgInboxInfoBean.messageType = msgInfo.type;
        msgInboxInfoBean.messageTypeText = MsgStatus.getMsgType(msgInfo.type);
        msgInboxInfoBean.readFlag = msgInfo.sys_status;
        msgInboxInfoBean.time = msgInfo.created_at;
        msgInboxInfoBean.timeTxt = TimeKit.format("yyyy-MM-dd HH:mm:ss",msgInfo.created_at);
        response.data = msgInboxInfoBean;
        return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
    }

    /**
     * 收取消息 （系统把消息 同步到用户收件箱,同时修改系统发件表的状态）
     * TODO 传参统一用小驼峰命名规则
     *
     * @paramss userId|用户ID(收件人)
     * @paramss userType|发件人类型，用户类型:个人用户 1/企业用户 2/代理人 3/业管用户 4
     * @return mixed
     * @access public
     */
    private String insertMsgRec(MsgRec msgRec) {
        BaseResponse response = new BaseResponse();
        //判空
        if (msgRec==null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        //TODO 查询消息发送对象表里的数据
        List<MsgSys> MsgSys = msgInboxDAO.findMsgToRecord(msgRec);
        //todo 查询消息 系统表有没有未插入的数据，没有的话，返回执行结束，有的话继续执行（赋值，插入，改变状态）
        //List<MsgSys> MsgSys = msgInboxDAO.findUserMsgRes(msgRec);
        //判断集合是否为空
        if (null == MsgSys || MsgSys.size() == 0) {
            return json(BaseResponse.CODE_SUCCESS, "未查看消息为空", response);
        }
        //获取当前时间戳(毫秒值)
        long date = new Date().getTime();
        List<Integer> insertResList = new ArrayList();
        for (MsgSys sys : MsgSys) {
            msgRec.msg_id = sys.id;
            MsgSys msgSysRes = msgInboxDAO.findMsgSysRes(sys);
            msgRec.type = msgSysRes.type;
            msgRec.sys_status = 1;//未读
            msgRec.state = 1;//未读
            msgRec.created_at = date;
            msgRec.updated_at = date;
            int insertRes = msgInboxDAO.insertMsgRec(msgRec);
            if (insertRes != 0) {
                MsgSys updateSys = new MsgSys();
                updateSys.id = sys.id;
                updateSys.status = 2;//已读
                updateSys.manager_uuid = msgRec.manager_uuid;
                updateSys.account_uuid = msgRec.account_uuid;
                int updateRes = msgInboxDAO.updateMsgToRecord(updateSys);
                //int updateRes = msgInboxDAO.updateMsgSysStatus(updateSys);
            }
            //TODO  更改msg_sys消息读取状态
            insertResList.add(insertRes);
        }
        response.data = insertResList;
        return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
    }

    /**
     * 消息 发件箱列表
     *
     * @paramss userId        用户id
     * @paramss userType      用户类型:用户类型:个人用户 1/企业用户 2/代理人 3/业管用户 4
     * @paramss messageStatus 消息 状态:未读 1/已读 2/全部 3/删除 4 （非必传，默认为1）
     * @paramss pageNum       当前页码 ，可不传，默认为1
     * @paramss lastId        上一页最大id ，可不传，默认为
     * @paramss limit         每页显示行数，可不传，默认为
     * @return json
     * @access public
     * TODO 发件箱要不要按分类展示
     */
    public String findMsgSysList(ActionBean actionBean) {
        MsgInboxBean.OutboxListRequest request = JsonKit.json2Bean(actionBean.body, MsgInboxBean.OutboxListRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        //调用DAO
        MsgSys msgSys = new MsgSys();
        msgSys.page = setPage(request.lastId, request.pageNum, request.limit);
        msgSys.status = request.messageStatus;
        msgSys.account_uuid = actionBean.accountUuid;
        msgSys.manager_uuid = actionBean.managerUuid;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<MsgTypeLists>  msgOutboxs = msgInboxDAO.findMsgSysList(msgSys);
        long newLastId = 0;
        if(msgOutboxs==null){
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
        List<MsgInboxListBean> msgInboxLists = new ArrayList<>();
        for (MsgTypeLists msgOutbox : msgOutboxs) {
            MsgInboxListBean msgInboxListBean = new MsgInboxListBean();
            msgInboxListBean.messageType = msgOutbox.type;
            newLastId = msgOutbox.mid;
            msgInboxListBean.messageTypeText = MsgStatus.getMsgType(msgOutbox.type);
            msgInboxListBean.unReadCount = msgOutbox.count;
            msgInboxListBean.unReadCountText = msgOutbox.count + "条新消息";
            msgInboxListBean.time = msgOutbox.time;
            msgInboxListBean.timeTxt = sdf.format(new Date(Long.valueOf(msgOutbox.time)));
            msgInboxLists.add(msgInboxListBean);
        }
        MsgCount msgCount = msgInboxDAO.findMsgSysCount(msgSys);
        MsgListbean msgListbean = new MsgListbean();
        msgListbean.countNum = msgCount.count_num;
        msgListbean.msgLists = msgInboxLists;
        response.data = msgListbean;
        int size = msgInboxLists.size();
        if (StringKit.isInteger(request.pageNum)) {
            response.page = setPageBean(request.pageNum, request.pageSize, 0, size);
        } else if (StringKit.isInteger(request.lastId)) {
            response.page = setPageBean(newLastId, request.pageSize, 0, size);
        }
        if (msgInboxLists != null) {
            return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
    }

    /**
     * 消息 发件箱列表-某一分类列表
     *
     * @paramss userId        用户id
     * @paramss userType      用户类型:个人用户 1/企业用户 2//代理人 3/业管用户4
     * @paramss messageStatus 消息 状态:未读 1/已读 2/全部 3/（非必传，默认为1）
     * @paramss messageType   消息 类型:系统通知1/保单助手2/理赔进度3/最新任务4/客户消息5/活动消息6/顾问消息7/'
     * @paramss pageNum       当前页码 ，可不传，默认为1
     * @paramss lastId        上一页最大id ，可不传，默认为
     * @paramss limit         每页显示行数，可不传，默认为
     * @return json
     * @access public
     */
    public String findMsgSysListByType(ActionBean actionBean) {
        MsgInboxBean.OutboxListRequest request = JsonKit.json2Bean(actionBean.body, MsgInboxBean.OutboxListRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        if (request.messageType == 0) {
            return json(BaseResponse.CODE_FAILURE, "messageType is empty", response);
        }
        //调用DAO
        MsgSys msgSys = new MsgSys();
        msgSys.page = setPage(request.lastId, request.pageNum, request.limit);
        if (request.messageStatus != 0) {
            msgSys.status = request.messageStatus;
        }
        msgSys.from_id = request.userId;
        msgSys.from_type = request.userType;
        msgSys.type = request.messageType;
        msgSys.account_uuid = actionBean.accountUuid;
        msgSys.manager_uuid = actionBean.managerUuid;
        List<MsgSys> msgResList = msgInboxDAO.findMsgSysListByType(msgSys);//获取当前分类下的消息主体信息
        long newLastId = 0;
        if(msgResList==null){
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<MsgInboxListTypeBean> msgInboxListTypeBeans = new ArrayList<>();
        for (MsgSys sys : msgResList) {
            msgSys.id = sys.id;
            List<MsgRecord> msgRecords = msgInboxDAO.findMsgTo(msgSys);//获取此消息的发送对象信息
            //todo 一个发件箱的发件人分为代理人和渠道
            List<MsgToBean> MsgToBeans = new ArrayList<>();
            for (MsgRecord msgRecord : msgRecords) {
                MsgToBean msgToBean = new MsgToBean();
                msgToBean.toId = msgRecord.rec_id;
                msgToBean.toType = msgRecord.type;
                switch(msgRecord.type){
                    case 4://代理人
                        AgentJobBean agentJobBean = agentJobClient.getAgentById(msgRecord.rec_id);
                        if(agentJobBean!=null){
                            logger.info("代理人姓名是"+agentJobBean.name);
                            msgToBean.toName = agentJobBean.name;
                        }
                        break;
                    case 5://渠道
                        ChannelBean channelBean = channelClient.getChannel(msgRecord.rec_id+"");
                        if(channelBean!=null){
                            logger.info("渠道姓名是"+channelBean.name);
                            msgToBean.toName = channelBean.name;
                        }
                        break;
                }
                MsgToBeans.add(msgToBean);
            }
            MsgInboxListTypeBean msgInboxListType = new MsgInboxListTypeBean();
            msgInboxListType.id = sys.id;
            newLastId = sys.id;
            msgInboxListType.title = sys.title;
            List<String> list = new ArrayList<>();
            String str[] =  sys.content.split("\n");
            list = Arrays.asList(str);
            msgInboxListType.content = list;
            msgInboxListType.messageType = sys.type;
            msgInboxListType.messageTypeText = MsgStatus.getMsgType(sys.type);
            msgInboxListType.readFlag = sys.status;
            msgInboxListType.time = sys.created_at;
            msgInboxListType.timeTxt = sdf.format(new Date(Long.valueOf(sys.created_at)));
            msgInboxListType.msgToBean = MsgToBeans;
            msgInboxListTypeBeans.add(msgInboxListType);
        }
        response.data = msgInboxListTypeBeans;
        int size = msgInboxListTypeBeans.size();
        if (StringKit.isInteger(request.pageNum)) {
            int total = msgInboxDAO.findMsgSysCountByType(msgSys);
            response.page = setPageBean(request.pageNum, request.pageSize, total, size);
        } else if (StringKit.isInteger(request.lastId)) {
            response.page = setPageBean(newLastId, request.pageSize, 0, size);
        }
        if (response.data != null) {
            return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
    }

    /**
     * 消息 详情
     *
     * @paramss messageId 消息 id
     * @return json
     * @access public
     */
    public String findMsgOutboxInfo(ActionBean actionBean) {
        MsgInboxBean.MsgInfoRequest request = JsonKit.json2Bean(actionBean.body, MsgInboxBean.MsgInfoRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        MsgSys msgSys = new MsgSys();
        msgSys.id = request.messageId;
        msgSys.manager_uuid = actionBean.managerUuid;
        msgSys.account_uuid = actionBean.accountUuid;
        MsgSys msgSysInfo = msgInboxDAO.findMsgSysInfo(msgSys);
        if(msgSysInfo==null){
            return json(BaseResponse.CODE_FAILURE, "not found msgInfo", response);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MsgInboxInfoBean msgInboxInfoBean = new MsgInboxInfoBean();
        msgInboxInfoBean.id = msgSysInfo.id;
        msgInboxInfoBean.title = msgSysInfo.title;
        List<String> list = new ArrayList<>();
        String str[] =  msgSysInfo.content.split("\n");
        list = Arrays.asList(str);
        msgInboxInfoBean.content = list;
        msgInboxInfoBean.messageType = msgSysInfo.type;
        msgInboxInfoBean.messageTypeText = MsgStatus.getMsgType(msgSysInfo.type);
        msgInboxInfoBean.readFlag = msgSysInfo.status;
        msgInboxInfoBean.time = msgSysInfo.created_at;
        msgInboxInfoBean.timeTxt = sdf.format(new Date(Long.valueOf(msgSysInfo.created_at)));
        List<MsgRecord> msgRecords = msgInboxDAO.findMsgTo(msgSys);
        List<MsgToBean> MsgToBeans = new ArrayList<>();
        for (MsgRecord msgRecord : msgRecords) {
            MsgToBean msgToBean = new MsgToBean();
            msgToBean.toId = msgRecord.rec_id;
            msgToBean.toType = msgRecord.type;
            switch(msgRecord.type){
                case 4://代理人
                    AgentJobBean agentJobBean = agentJobClient.getAgentById(msgRecord.rec_id);
                    if(agentJobBean!=null){
                        logger.info("代理人姓名是"+agentJobBean.name);
                        msgToBean.toName = agentJobBean.name;
                    }
                    break;
                case 5://渠道
                    ChannelBean channelBean = channelClient.getChannel(msgRecord.rec_id+"");
                    if(channelBean!=null){
                        logger.info("渠道姓名是"+channelBean.name);
                        msgToBean.toName = channelBean.name;
                    }
                    break;
            }
            MsgToBeans.add(msgToBean);
        }
        msgInboxInfoBean.msgToBean = MsgToBeans;
        response.data = msgInboxInfoBean;
        if (msgSysInfo != null) {
            return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "not found msgInfo", response);
        }
    }

}
