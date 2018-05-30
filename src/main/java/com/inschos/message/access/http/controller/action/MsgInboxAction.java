package com.inschos.message.access.http.controller.action;

import com.inschos.message.access.http.controller.bean.*;
import com.inschos.message.assist.kit.StringKit;
import com.inschos.message.data.dao.MsgInboxDAO;
import com.inschos.message.assist.kit.JsonKit;
import com.inschos.message.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class MsgInboxAction extends BaseAction {
    private static final Logger logger = Logger.getLogger(MsgInboxAction.class);
    @Autowired
    private MsgInboxDAO msgInboxDAO;
    private Page page;

    /**
     * 消息 收件箱列表(获取所有类型的列表)
     *
     * @param userId        用户id
     * @param userType      用户类型:个人用户 1/企业用户 2//代理人 3/业管用户4
     * @param messageStatus 消息 状态:未读1/已读2/全部3/删除4（非必传，默认为1）
     * @param pageNum       当前页码 ，可不传，默认为1
     * @param lastId        上一页最大id ，可不传，默认为
     * @param limit         每页显示行数，可不传，默认为
     * @return json
     * <p>
     * 业管可以查看所有人、所有类型的消息，返回按消息分类展示
     * 消息 列表组成：消息 系统表里收件人id为-1的（系统消息）+ 消息 系统表里收件人id为user_id的（订阅消息、私信）
     * 匹配消息 系统表和消息 收件箱表，向用户收件箱里插入相应的数据，并修改消息 系统表的状态
     * todo 只要用户接收消息 ，系统表就默认已经读取了，不在插入
     * @access public
     */
    public String findMsgRecList(ActionBean actionBean) {
        MsgInboxBean.InboxListRequest request = JsonKit.json2Bean(actionBean.body, MsgInboxBean.InboxListRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        //调用DAO
        MsgRec msgRec = new MsgRec();
        msgRec.page = setPage(request.lastId, request.pageNum, request.limit);
        if (request.messageStatus != 0) {
            msgRec.sys_status = request.messageStatus;
        }
        msgRec.user_id = request.userId;
        msgRec.user_type = request.userType;
        MsgStatus msgStatus = new MsgStatus();
        //根据user_type判断不同用户可以查看消息 类型
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        switch (request.userType) {
            case 4://业管用户-查看的收件箱列表：所有用户的和发给业管自己的
                List<MsgTypeLists> msgInboxManager = msgInboxDAO.findMsgRecList(msgRec);
                List<MsgInboxListBean> msgInboxListManagers = new ArrayList<>();
                for (MsgTypeLists msgTypeLists : msgInboxManager) {
                    MsgInboxListBean msgInboxListManager = new MsgInboxListBean();
                    msgInboxListManager.messageType = msgTypeLists.type;
                    msgInboxListManager.messageTypeText = MsgStatus.getMsgType(msgTypeLists.type);
                    msgInboxListManager.unReadCount = msgTypeLists.count;
                    msgInboxListManager.unReadCountText = msgTypeLists.count + "条新消息";
                    msgInboxListManager.time = msgTypeLists.time;
                    msgInboxListManager.timeTxt = sdf.format(new Date(Long.valueOf(msgTypeLists.time)));
                    msgInboxListManagers.add(msgInboxListManager);
                }
                response.data = msgInboxListManagers;
                break;
            case 3://企业用户
                String insertResCompany = insertMsgRec(request.userId, request.userType);
                List<MsgTypeLists> msgInboxCompany = msgInboxDAO.findMsgRecList(msgRec);
                List<MsgInboxListBean> msgInboxListCompanys = new ArrayList<>();
                for (MsgTypeLists msgTypeLists : msgInboxCompany) {
                    MsgInboxListBean msgInboxListCompany = new MsgInboxListBean();
                    msgInboxListCompany.messageType = msgTypeLists.type;
                    msgInboxListCompany.messageTypeText = MsgStatus.getMsgType(msgTypeLists.type);
                    msgInboxListCompany.unReadCount = msgTypeLists.count;
                    msgInboxListCompany.unReadCountText = msgTypeLists.count + "条新消息";
                    msgInboxListCompany.time = msgTypeLists.time;
                    msgInboxListCompany.timeTxt = sdf.format(new Date(Long.valueOf(msgTypeLists.time)));
                    msgInboxListCompanys.add(msgInboxListCompany);
                }
                response.data = msgInboxListCompanys;
                break;
            case 2://代理人用户
                String insertResAgent = insertMsgRec(request.userId, request.userType);
                List<MsgTypeLists> msgInboxAgent = msgInboxDAO.findMsgRecList(msgRec);
                List<MsgInboxListBean> msgInboxListAgents = new ArrayList<>();

                for (MsgTypeLists msgTypeLists : msgInboxAgent) {
                    MsgInboxListBean msgInboxListAgent = new MsgInboxListBean();
                    msgInboxListAgent.messageType = msgTypeLists.type;
                    msgInboxListAgent.messageTypeText = MsgStatus.getMsgType(msgTypeLists.type);
                    msgInboxListAgent.unReadCount = msgTypeLists.count;
                    msgInboxListAgent.unReadCountText = msgTypeLists.count + "条新消息";
                    msgInboxListAgent.time = msgTypeLists.time;
                    msgInboxListAgent.timeTxt = sdf.format(new Date(Long.valueOf(msgTypeLists.time)));
                    msgInboxListAgents.add(msgInboxListAgent);
                }
                response.data = msgInboxListAgents;
                break;
            case 1://个人用户-判断登录信息，再向收件箱表里插入数据
                String insertResPerson = insertMsgRec(request.userId, request.userType);
                List<MsgTypeLists> msgInboxPerson = msgInboxDAO.findMsgRecList(msgRec);
                List<MsgInboxListBean> msgInboxListPersons = new ArrayList<>();
                for (MsgTypeLists msgTypeLists : msgInboxPerson) {
                    MsgInboxListBean msgInboxListPerson = new MsgInboxListBean();
                    msgInboxListPerson.messageType = msgTypeLists.type;
                    msgInboxListPerson.messageTypeText = MsgStatus.getMsgType(msgTypeLists.type);
                    msgInboxListPerson.unReadCount = msgTypeLists.count;
                    msgInboxListPerson.unReadCountText = msgTypeLists.count + "条新消息";
                    msgInboxListPerson.time = msgTypeLists.time;
                    msgInboxListPerson.timeTxt = sdf.format(new Date(Long.valueOf(msgTypeLists.time)));
                    msgInboxListPersons.add(msgInboxListPerson);
                }
                response.data = msgInboxListPersons;
                break;
            default:
                response.data = "";
                break;
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
     * @param userId        用户id
     * @param userType      用户类型:个人用户 1/企业用户 2//代理人 3/业管用户4
     * @param messageStatus 消息 状态:未读 1/已读 2/全部 3/（非必传，默认为1）
     * @param messageType   消息 类型:系统通知1/保单助手2/理赔进度3/最新任务4/客户消息5/活动消息6/顾问消息7/'
     * @param pageNum       当前页码 ，可不传，默认为1
     * @param lastId        上一页最大id ，可不传，默认为
     * @param limit         每页显示行数，可不传，默认为
     * @return json
     * 消息 列表组成：消息 系统表里收件人id为-1的（系统消息）+ 消息 系统表里收件人id为user_id的（订阅消息、私信）
     * 匹配消息 系统表和消息 收件箱表，向用户收件箱里插入相应的数据，并修改消息 系统表的状态
     * todo 只要用户接收消息 ，系统表就默认已经读取了，不在插入
     * @access public
     */
    public String findMsgResListByType(ActionBean actionBean) {
        MsgInboxBean.InboxListRequest request = JsonKit.json2Bean(actionBean.body, MsgInboxBean.InboxListRequest.class);
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
        msgRec.user_id = request.userId;
        msgRec.user_type = request.userType;
        msgRec.type = request.messageType;
        MsgStatus msgStatus = new MsgStatus();
        MsgInboxListTypeBean msgInboxListTypeBean = new MsgInboxListTypeBean();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //根据user_type判断不同用户可以查看消息 类型
        switch (request.userType) {
            case 4://业管用户-查看的收件箱列表：所有用户的和发给业管自己的
                List<MsgRec> msgInboxManager = msgInboxDAO.findMsgRecListByType(msgRec);
                List<MsgInboxListTypeBean> msgInboxListTypeManagers = new ArrayList<>();
                for (MsgRec msgInboxRes : msgInboxManager) {
                    MsgInboxListTypeBean msgInboxListTypeManager = new MsgInboxListTypeBean();
                    msgInboxListTypeManager.id = msgInboxRes.id;
                    msgInboxListTypeManager.title = msgInboxRes.msgSys.title;
                    msgInboxListTypeManager.content = msgInboxRes.msgSys.content;
                    msgInboxListTypeManager.messageType = msgInboxRes.type;
                    msgInboxListTypeManager.messageTypeText = MsgStatus.getMsgType(msgInboxRes.type);
                    msgInboxListTypeManager.readFlag = msgInboxRes.sys_status;
                    msgInboxListTypeManager.time = msgInboxRes.created_at;
                    msgInboxListTypeManager.timeTxt = sdf.format(new Date(Long.valueOf(msgInboxRes.created_at)));
                    msgInboxListTypeManagers.add(msgInboxListTypeManager);
                }
                response.data = msgInboxListTypeManagers;
                break;
            case 3://企业用户
                String insertResCompany = insertMsgRec(request.userId, request.userType);
                List<MsgRec> msgInboxCompany = msgInboxDAO.findMsgRecListByType(msgRec);
                List<MsgInboxListTypeBean> msgInboxListTypeCompanys = new ArrayList<>();
                for (MsgRec msgInboxRes : msgInboxCompany) {
                    MsgInboxListTypeBean msgInboxListTypeCompany = new MsgInboxListTypeBean();
                    msgInboxListTypeCompany.id = msgInboxRes.id;
                    msgInboxListTypeCompany.title = msgInboxRes.msgSys.title;
                    msgInboxListTypeCompany.content = msgInboxRes.msgSys.content;
                    msgInboxListTypeCompany.messageType = msgInboxRes.type;
                    msgInboxListTypeCompany.messageTypeText = MsgStatus.getMsgType(msgInboxRes.type);
                    msgInboxListTypeCompany.readFlag = msgInboxRes.sys_status;
                    msgInboxListTypeCompany.time = msgInboxRes.created_at;
                    msgInboxListTypeCompany.timeTxt = sdf.format(new Date(Long.valueOf(msgInboxRes.created_at)));
                    msgInboxListTypeCompanys.add(msgInboxListTypeCompany);
                }
                response.data = msgInboxListTypeCompanys;
                break;
            case 2://代理人用户
                String insertResAgent = insertMsgRec(request.userId, request.userType);
                List<MsgRec> msgInboxAgent = msgInboxDAO.findMsgRecListByType(msgRec);
                List<MsgInboxListTypeBean> msgInboxListTypeAgents = new ArrayList<>();
                for (MsgRec msgInboxRes : msgInboxAgent) {
                    MsgInboxListTypeBean msgInboxListTypeAgent = new MsgInboxListTypeBean();
                    msgInboxListTypeAgent.id = msgInboxRes.id;
                    msgInboxListTypeAgent.title = msgInboxRes.msgSys.title;
                    msgInboxListTypeAgent.content = msgInboxRes.msgSys.content;
                    msgInboxListTypeAgent.messageType = msgInboxRes.type;
                    msgInboxListTypeAgent.messageTypeText = MsgStatus.getMsgType(msgInboxRes.type);
                    msgInboxListTypeAgent.readFlag = msgInboxRes.sys_status;
                    msgInboxListTypeAgent.time = msgInboxRes.created_at;
                    msgInboxListTypeAgent.timeTxt = sdf.format(new Date(Long.valueOf(msgInboxRes.created_at)));
                    msgInboxListTypeAgents.add(msgInboxListTypeAgent);
                }
                response.data = msgInboxListTypeAgents;
                break;
            case 1://个人用户-判断登录信息，再向收件箱表里插入数据
                String insertResPerson = insertMsgRec(request.userId, request.userType);
                List<MsgRec> msgInboxPerson = msgInboxDAO.findMsgRecListByType(msgRec);
                List<MsgInboxListTypeBean> msgInboxListTypePersons = new ArrayList<>();
                for (MsgRec msgInboxRes : msgInboxPerson) {
                    MsgInboxListTypeBean msgInboxListTypePerson = new MsgInboxListTypeBean();
                    msgInboxListTypePerson.id = msgInboxRes.id;
                    msgInboxListTypePerson.title = msgInboxRes.msgSys.title;
                    msgInboxListTypePerson.content = msgInboxRes.msgSys.content;
                    msgInboxListTypePerson.messageType = msgInboxRes.type;
                    msgInboxListTypePerson.messageTypeText = MsgStatus.getMsgType(msgInboxRes.type);
                    msgInboxListTypePerson.readFlag = msgInboxRes.sys_status;
                    msgInboxListTypePerson.time = msgInboxRes.created_at;
                    msgInboxListTypePerson.timeTxt = sdf.format(new Date(Long.valueOf(msgInboxRes.created_at)));
                    msgInboxListTypePersons.add(msgInboxListTypePerson);
                }
                response.data = msgInboxListTypePersons;
                break;
            default:
                response.data = "";
                break;
        }
        if (response.data != null) {
            return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
    }

    /**
     * 消息 根据parentId获取消息列表
     * TODO  先不改
     * @param userId        用户id
     * @param userType      用户类型:个人用户 1/企业用户 2/代理人 3/业管用户4
     * @param messageStatus 消息 状态:未读 1/已读 2/全部 3/（非必传，默认为1）
     * @param messageType   消息 类型:客户消息5/顾问消息7/,默认是顾问消息和客户消息，根据用户类型判断
     * @param parentId
     * @param pageNum       当前页码 ，可不传，默认为1
     * @param lastId        上一页最大id ，可不传，默认为
     * @param limit         每页显示行数，可不传，默认为
     * @return json
     * @access public
     */
    public String findMsgResListByParent(ActionBean actionBean) {
        MsgInboxBean.InboxListRequest request = JsonKit.json2Bean(actionBean.body, MsgInboxBean.InboxListRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        if (request.messageType == 0) {
            return json(BaseResponse.CODE_FAILURE, "messageType is empty", response);
        }
        if (request.parentId == 0) {
            return json(BaseResponse.CODE_FAILURE, "parentId is empty", response);
        }
        //调用DAO
        MsgRec msgRec = new MsgRec();
        msgRec.page = setPage(request.lastId, request.pageNum, request.limit);
        msgRec.sys_status = request.messageStatus;
        msgRec.user_id = request.userId;
        msgRec.user_type = request.userType;
        msgRec.type = request.messageType;
        msgRec.parent_id = request.parentId;
        MsgStatus msgStatus = new MsgStatus();
        logger.info(msgRec.user_id);
        logger.info(msgRec.user_type);
        logger.info(msgRec.type);
        logger.info(msgRec.parent_id);
        //根据user_type判断不同用户可以查看消息 类型
        //TODO 判断登录
        int loginStatus = 1;
        switch (request.userType) {
            //TODO 还不确定业管和企业用户是否可以查看
//            case 4://业管用户-查看的收件箱列表：所有用户的和发给业管自己的
//                List<MsgTypeLists> msgInboxManager = msgInboxDAO.findMsgRecList(msgRec);
//                response.data = msgInboxManager;
//                break;
//            case 3://企业用户
//                if (loginStatus != 0) {
//                    String insertRes = insertMsgRec(request.userId, request.userType);
//                }
//                List<MsgTypeLists> msgInboxCompany = msgInboxDAO.findMsgRecList(msgRec);
//                response.data = msgInboxCompany;
//                break;
            case 2://代理人用户
                if (request.messageType != 5) {
                    return json(BaseResponse.CODE_FAILURE, "messageStatus is error", response);
                }
                if (loginStatus != 0) {
                    String insertRes = insertMsgRec(request.userId, request.userType);
                }
                List<MsgRec> msgInboxAgent = msgInboxDAO.findMsgRecListByParent(msgRec);
                response.data = msgInboxAgent;
                break;
            case 1://个人用户-判断登录信息，再向收件箱表里插入数据
                if (request.messageType != 7) {
                    return json(BaseResponse.CODE_FAILURE, "messageStatus is error", response);
                }
                if (loginStatus != 0) {
                    String insertRes = insertMsgRec(request.userId, request.userType);
                }
                List<MsgRec> msgInboxPerson = msgInboxDAO.findMsgRecListByParent(msgRec);
                response.data = msgInboxPerson;
                break;
            default:
                response.data = "";
                break;
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
     * @param messageId 消息 id
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MsgRec msgRec = new MsgRec();
        MsgSys msgSys = new MsgSys();
        msgRec.id = request.messageId;
        MsgRec msgInfo = msgInboxDAO.findMsgInfo(msgRec);
        MsgInboxInfoBean msgInboxInfoBean = new MsgInboxInfoBean();
        msgInboxInfoBean.id = msgInfo.id;
        msgInboxInfoBean.title = msgInfo.msgSys.title;
        msgInboxInfoBean.content = msgInfo.msgSys.content;
        msgInboxInfoBean.messageType = msgInfo.type;
        msgInboxInfoBean.messageTypeText = MsgStatus.getMsgType(msgInfo.type);
        msgInboxInfoBean.readFlag = msgInfo.sys_status;
        msgInboxInfoBean.time = msgInfo.created_at;
        msgInboxInfoBean.timeTxt = sdf.format(new Date(Long.valueOf(msgInfo.created_at)));
        msgSys.id = msgInfo.msg_id;
        List<MsgTo> msgTo = msgInboxDAO.findMsgTo(msgSys);
        List<MsgToBean> MsgToBeans = new ArrayList<>();
        for (MsgTo to : msgTo) {
            MsgToBean msgToBean = new MsgToBean();
            msgToBean.toId = to.to_id;
            msgToBean.toType = to.to_type;
            msgToBean.channelId = to.channel_id;
            MsgToBeans.add(msgToBean);
        }
        msgInboxInfoBean.msgToBean = MsgToBeans;
        response.data = msgInboxInfoBean;
        if (msgInfo != null) {
            return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "not found msgInfo", response);
        }
    }

    /**
     * 收取消息 （系统把消息 同步到用户收件箱,同时修改系统发件表的状态）
     * TODO 传参统一用小驼峰命名规则
     *
     * @param userId|用户ID(收件人)
     * @param userType|发件人类型，用户类型:个人用户 1/企业用户 2/代理人 3/业管用户 4
     * @return mixed
     * @access public
     */
    public String insertMsgRec(long userId, int userType) {
        BaseResponse response = new BaseResponse();
        //判空
        if (userId == 0 || userType == 0) {
            return json(BaseResponse.CODE_FAILURE, "userId or userType is empty", response);
        }
        //查询消息 系统表有没有未插入的数据，没有的话，返回执行结束，有的话继续执行（赋值，插入，改变状态）
        MsgRec msgRec = new MsgRec();
        msgRec.user_id = userId;
        msgRec.user_type = userType;
        List<MsgSys> MsgSys = msgInboxDAO.findUserMsgRes(msgRec);
        //判断集合是否为空
        if (null == MsgSys || MsgSys.size() == 0) {
            return json(BaseResponse.CODE_SUCCESS, "未查看消息为空", response);
        }
        //获取当前时间戳(毫秒值)
        long date = new Date().getTime();
        List insertResList = new ArrayList();
        for (MsgSys sys : MsgSys) {
            msgRec.msg_id = sys.id;
            msgRec.type = sys.type;
            msgRec.parent_id = sys.parent_id;
            msgRec.sys_status = 0;
            msgRec.state = 0;
            msgRec.created_at = date;
            msgRec.updated_at = date;
            int insertRes = msgInboxDAO.insertMsgRec(msgRec);
            if (insertRes != 0) {
                MsgSys updateSys = new MsgSys();
                updateSys.id = sys.id;
                updateSys.status = 1;
                int updateRes = msgInboxDAO.updateMsgSysStatus(updateSys);
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
     * @param userId        用户id
     * @param userType      用户类型:用户类型:个人用户 1/企业用户 2/代理人 3/业管用户 4
     * @param messageStatus 消息 状态:未读 1/已读 2/全部 3/删除 4 （非必传，默认为1）
     * @param pageNum       当前页码 ，可不传，默认为1
     * @param lastId        上一页最大id ，可不传，默认为
     * @param limit         每页显示行数，可不传，默认为
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
        msgSys.from_id = request.userId;
        msgSys.from_type = request.userType;
        List<MsgSys> msgOutboxs = msgInboxDAO.findMsgSysList(msgSys);
        response.data = msgOutboxs;
        if (msgOutboxs != null) {
            return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
    }

    /**
     * 消息 发件箱列表-某一分类列表
     *
     * @param userId        用户id
     * @param userType      用户类型:个人用户 1/企业用户 2//代理人 3/业管用户4
     * @param messageStatus 消息 状态:未读 1/已读 2/全部 3/（非必传，默认为1）
     * @param messageType   消息 类型:系统通知1/保单助手2/理赔进度3/最新任务4/客户消息5/活动消息6/顾问消息7/'
     * @param pageNum       当前页码 ，可不传，默认为1
     * @param lastId        上一页最大id ，可不传，默认为
     * @param limit         每页显示行数，可不传，默认为
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
        MsgStatus msgStatus = new MsgStatus();
        List<MsgSys> msgRes = msgInboxDAO.findMsgSysListByType(msgSys);
        response.data = msgRes;
        if (response.data != null) {
            return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
    }

    /**
     * 消息 根据parentId获取消息列表
     *
     * @param userId        用户id
     * @param userType      用户类型:个人用户 1/企业用户 2//代理人 3/业管用户4
     * @param messageStatus 消息 状态:未读 1/已读 2/全部 3/（非必传，默认为1）
     * @param messageType   消息 类型:系统通知1/保单助手2/理赔进度3/最新任务4/客户消息5/活动消息6/顾问消息7/'
     * @param parentId
     * @param pageNum       当前页码 ，可不传，默认为1
     * @param lastId        上一页最大id ，可不传，默认为
     * @param limit         每页显示行数，可不传，默认为
     * @return json
     * @access public
     */
    public String findMsgSysListByParent(ActionBean actionBean) {
        MsgInboxBean.OutboxListRequest request = JsonKit.json2Bean(actionBean.body, MsgInboxBean.OutboxListRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        if (request.messageType == 0) {
            return json(BaseResponse.CODE_FAILURE, "messageType is empty", response);
        }
        if (request.parentId == 0) {
            return json(BaseResponse.CODE_FAILURE, "parentId is empty", response);
        }
        //调用DAO
        MsgSys msgSys = new MsgSys();
        msgSys.page = setPage(request.lastId, request.pageNum, request.limit);
        msgSys.status = request.messageStatus;
        msgSys.from_id = request.userId;
        msgSys.from_type = request.userType;
        msgSys.type = request.messageType;
        msgSys.parent_id = request.parentId;
        MsgStatus msgStatus = new MsgStatus();
        //根据user_type判断不同用户可以查看消息 类型
        switch (request.userType) {
            //TODO 还不确定业管和企业用户是否可以查看
//            case 4://业管用户-查看的收件箱列表：所有用户的和发给业管自己的
//               List<MsgSys> msgOutboxManager = msgInboxDAO.findMsgSysListByParent(msgSys);
//                response.data = msgOutboxManager;
//                break;
//            case 3://企业用户
//                List<MsgSys> msgOutboxCompany = msgInboxDAO.findMsgSysListByParent(msgSys);
//                response.data = msgOutboxCompany;
//                break;
            case 2://代理人用户
                if (request.messageType != 5) {
                    return json(BaseResponse.CODE_FAILURE, "messageStatus is error", response);
                }
                List<MsgSys> msgOutboxAgent = msgInboxDAO.findMsgSysListByParent(msgSys);
                response.data = msgOutboxAgent;
                break;
            case 1://个人用户-判断登录信息，再向收件箱表里插入数据
                if (request.messageType != 7) {
                    return json(BaseResponse.CODE_FAILURE, "messageStatus is error", response);
                }
                List<MsgSys> msgOutboxPerson = msgInboxDAO.findMsgSysListByParent(msgSys);
                response.data = msgOutboxPerson;
                break;
            default:
                response.data = "";
                break;
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
     * @param messageId 消息 id
     * @return json
     * @access public
     */
    public String findMsgOutboxInfo(ActionBean actionBean){
        MsgInboxBean.MsgInfoRequest request = JsonKit.json2Bean(actionBean.body, MsgInboxBean.MsgInfoRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        MsgRec msgRec = new MsgRec();
        msgRec.id = request.messageId;
        MsgRec msgInfo = msgInboxDAO.findMsgInfo(msgRec);
        response.data = msgInfo;
        if (msgInfo != null) {
            return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
        } else {
            MsgSys msgSys = new MsgSys();
            msgSys.id = request.messageId;
            MsgSys msgSysInfo = msgInboxDAO.findMsgSysInfo(msgSys);
            if (msgSysInfo != null) {
                response.data = msgSysInfo;
                return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
            } else {
                response.data = "";
                return json(BaseResponse.CODE_FAILURE, "not found msgInfo", response);
            }
        }
    }

}
