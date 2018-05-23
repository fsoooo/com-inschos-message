package com.inschos.message.access.http.controller.action;

import com.inschos.message.access.http.controller.bean.*;
import com.inschos.message.data.dao.MsgInboxDAO;
import com.inschos.message.assist.kit.JsonKit;
import com.inschos.message.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
     * @param messageStatus 消息 状态:未读 1/已读 2/全部 3/（非必传，默认为1）
     * @param pageNum       当前页码 ，可不传，默认为1
     * @param lastId        上一页最大id ，可不传，默认为
     * @param limit         每页显示行数，可不传，默认为
     * @return json
     * <p>
     * 业管可以查看所有人的消息，返回按消息分类展示
     * 消息 列表组成：消息 系统表里收件人id为-1的（系统消息）+ 消息 系统表里收件人id为user_id的（订阅消息、私信）
     * 匹配消息 系统表和消息 收件箱表，向用户收件箱里插入相应的数据，并修改消息 系统表的状态
     * todo 只要用户接收消息 ，系统表就默认已经读取了，不在插入
     * @access public
     */
    public String findMsgRecList(ActionBean actionBean) {
        MsgInboxBean.inboxListRequest request = JsonKit.json2Bean(actionBean.body, MsgInboxBean.inboxListRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        //调用DAO
        MsgRec msgRec = new MsgRec();
        msgRec.page = setPage(request.lastId, request.pageNum, request.limit);
        msgRec.sys_status = request.messageStatus;
        msgRec.user_id = request.userId;
        msgRec.user_type = request.userType;
        MsgStatus msgStatus = new MsgStatus();
        //根据user_type判断不同用户可以查看消息 类型
        switch (request.userType) {
            case 1://业管用户-查看的收件箱列表：所有用户的和发给业管自己的
                List<MsgRec> msgInboxManager = msgInboxDAO.findMsgRecList(msgRec);
                response.data = msgInboxManager;
                break;
            case 2://企业用户
                List<MsgRec> msgInboxCompany = msgInboxDAO.findMsgRecList(msgRec);
                response.data = msgInboxCompany;
                break;
            case 3://代理人用户
                List<MsgRec> msgInboxAgent = msgInboxDAO.findMsgRecList(msgRec);
                response.data = msgInboxAgent;
                break;
            case 4://个人用户-判断登录信息，再向收件箱表里插入数据
                //TODO 判断登录
                int loginStatus = 1;
                if (loginStatus != 0) {
                    String insertRes = insertMsgRec(request.userId, request.userType);
                }
                List<MsgRec> msgInboxPerson = msgInboxDAO.findMsgRecList(msgRec);
                response.data = msgInboxPerson;
                break;
            default:
                List<MsgRec> msgInboxs = msgInboxDAO.findMsgRecList(msgRec);
                response.data = msgInboxs;
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
        MsgInboxBean.inboxListRequest request = JsonKit.json2Bean(actionBean.body, MsgInboxBean.inboxListRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        //调用DAO
        MsgRec msgRec = new MsgRec();
        msgRec.page = setPage(request.lastId, request.pageNum, request.limit);
        msgRec.sys_status = request.messageStatus;
        msgRec.user_id = request.userId;
        msgRec.user_type = request.userType;
        MsgStatus msgStatus = new MsgStatus();
        //根据user_type判断不同用户可以查看消息 类型
        switch (request.userType) {
            case 1://业管用户-查看的收件箱列表：所有用户的和发给业管自己的
                List<MsgRec> msgInboxManager = msgInboxDAO.findMsgRecList(msgRec);
                response.data = msgInboxManager;
                break;
            case 2://企业用户
                List<MsgRec> msgInboxCompany = msgInboxDAO.findMsgRecList(msgRec);
                response.data = msgInboxCompany;
                break;
            case 3://代理人用户
                List<MsgRec> msgInboxAgent = msgInboxDAO.findMsgRecList(msgRec);
                response.data = msgInboxAgent;
                break;
            case 4://个人用户-判断登录信息，再向收件箱表里插入数据
                //TODO 判断登录
                int loginStatus = 1;
                if (loginStatus != 0) {
                    String insertRes = insertMsgRec(request.userId, request.userType);
                }
                List<MsgRec> msgInboxPerson = msgInboxDAO.findMsgRecList(msgRec);
                response.data = msgInboxPerson;
                break;
            default:
                List<MsgRec> msgInboxs = msgInboxDAO.findMsgRecList(msgRec);
                response.data = msgInboxs;
                break;
        }
        if (response.data != null) {
            return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
    }


    /**
     * 收取消息 （系统把消息 同步到用户收件箱,同时修改系统发件表的状态）
     * TODO 传参统一用小驼峰命名规则
     *
     * @param user_id|用户ID(收件人)
     * @param user_type|发件人类型，个人用户1/企业用户2/业管用户等
     * @return mixed
     * @access public
     */
    public String insertMsgRec(long user_id, int user_type) {
        BaseResponse response = new BaseResponse();
        //判空
        if (user_id == 0 || user_type == 0) {
            return json(BaseResponse.CODE_FAILURE, "user_id or user_type is empty", response);
        }
        //查询消息 系统表有没有未插入的数据，没有的话，返回执行结束，有的话继续执行（赋值，插入，改变状态）
        MsgRec msgRec = new MsgRec();
        msgRec.user_id = user_id;
        msgRec.user_type = user_type;
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
     * @param user_id        用户id
     * @param user_type      用户类型:个人用户 3/代理人 2/企业用户 1/业管用户 0
     * @param message_status 消息 状态:未读 0/已读 1/全部 2/删除 3 （非必传，默认为0）
     * @param page           当前页码 ，可不传，默认为1
     * @param last_id        上一页最大id ，可不传，默认为
     * @param limit          每页显示行数，可不传，默认为
     * @return json
     * @access public
     */
    public String findMsgSysList(ActionBean actionBean) {
        MsgInboxBean.outboxListRequest request = JsonKit.json2Bean(actionBean.body, MsgInboxBean.outboxListRequest.class);
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
     * 消息 详情
     *
     * @param message_id 消息 id
     * @return json
     * @access public
     */
    public String findMsgInfo(ActionBean actionBean) {
        MsgInboxBean.msgInfoRequest request = JsonKit.json2Bean(actionBean.body, MsgInboxBean.msgInfoRequest.class);
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

    /**
     * 操作消息 （收件箱 读取和删除）
     *
     * @param message_id   消息 id
     * @param operate_id   操作代码:默认为1（删除/已读），2（还原/未读）
     * @param operate_type 操作类型:read 更改读取状态，del 更改删除状态
     * @return json
     * @access public
     */
    public String updateMsgRec(ActionBean actionBean) {
        MsgInboxBean.msgUpdateRequest request = JsonKit.json2Bean(actionBean.body, MsgInboxBean.msgUpdateRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        //调用DAO
        MsgUpdate msgUpdate = new MsgUpdate();
        msgUpdate.msg_id = request.messageId;
        msgUpdate.operate_id = request.operateId;
        if (request.operateType == "read") {
            msgUpdate.operate_type = "sys_status";
            response.data = msgInboxDAO.updateMsgRecStatus(msgUpdate);
        } else if (request.operateType == "del") {
            msgUpdate.operate_type = "state";
            response.data = msgInboxDAO.updateMsgRecState(msgUpdate);
        }

        if (response.data != null) {
            return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
    }
}
