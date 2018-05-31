package com.inschos.message.access.http.controller.action;

import com.inschos.message.access.http.controller.bean.ActionBean;
import com.inschos.message.access.http.controller.bean.BaseRequest;
import com.inschos.message.access.http.controller.bean.BaseResponse;
import com.inschos.message.access.http.controller.bean.WorkOrderBean;
import com.inschos.message.access.rpc.bean.AccountBean;
import com.inschos.message.access.rpc.bean.AgentJobBean;
import com.inschos.message.access.rpc.bean.ChannelBean;
import com.inschos.message.access.rpc.client.AccountClient;
import com.inschos.message.access.rpc.client.AgentJobClient;
import com.inschos.message.access.rpc.client.ChannelClient;
import com.inschos.message.annotation.CheckParamsKit;
import com.inschos.message.assist.kit.StringKit;
import com.inschos.message.assist.kit.TimeKit;
import com.inschos.message.data.dao.WorkOrderCategoryDao;
import com.inschos.message.data.dao.WorkOrderDao;
import com.inschos.message.model.WorkOrder;
import com.inschos.message.model.WorkOrderCategory;
import com.inschos.message.model.WorkOrderReply;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class WorkOrderAction extends BaseAction {
    private static final Logger logger = Logger.getLogger(MsgModelAction.class);
    @Autowired
    private WorkOrderDao workOrderDao;
    @Autowired
    private AgentJobClient agentJobClient;
    @Autowired
    private ChannelClient channelClient;
    @Autowired
    private AccountClient accountClient;
    @Autowired
    private WorkOrderCategoryDao workOrderCategoryDao;


    public String untreatedCount(ActionBean actionBean){
        WorkOrderBean.UntreatedCountResponse response = new WorkOrderBean.UntreatedCountResponse();
        WorkOrder workOrder = new WorkOrder();
        workOrder.addressee_uuid = actionBean.accountUuid;
        workOrder.handle_status = 1;
        workOrder.type = WorkOrder.TYPE_MANAGER;

        int count = workOrderDao.untreatedCount(workOrder);

        response.count = count;

        return json(BaseResponse.CODE_SUCCESS, "获取成功", response);

    }

    public String listToMe(ActionBean bean,String method) {
        WorkOrderBean.WorkOrderListRequest request = requst2Bean(bean.body, WorkOrderBean.WorkOrderListRequest.class);
        WorkOrderBean.WorkOrderListResponse response = new WorkOrderBean.WorkOrderListResponse();

        List<CheckParamsKit.Entry<String, String>> entries = checkParams(request);
        if (entries != null) {
            return json(BaseResponse.CODE_PARAM_ERROR, entries, response);
        }
        WorkOrder search = new WorkOrder();
        if("closed".equals(method)){
            search.close_status = WorkOrder.STATUS_CLOSED;
        }else if("wait".equals(method)){
            search.close_status = WorkOrder.STATUS_HANDLE_WAITING;
        }else{
            return json(BaseResponse.CODE_PARAM_ERROR, "请选择查询类型", response);
        }

        search.addressee_uuid = bean.accountUuid;
        search.type = WorkOrder.TYPE_MANAGER;
        search.page = setPage(request.lastId, request.pageNum, request.pageSize);
        List<WorkOrder> orderList = workOrderDao.findPage(search);
        List<WorkOrderBean.WorkOrderData> dataList = new ArrayList<>();
        long newLastId = 0;
        if (orderList != null && !orderList.isEmpty()) {
            for (WorkOrder order : orderList) {

                WorkOrderBean.WorkOrderData data = toData(order,true,true);
                newLastId = order.id;
                dataList.add(data);
            }
        }
        response.data = dataList;
        int size = dataList.size();
        if (StringKit.isInteger(request.pageNum)) {
            int total = workOrderDao.findCount(search);
            response.page = setPageBean(request.pageNum, request.pageSize, total, size);
        } else if (StringKit.isInteger(request.lastId)) {
            response.page = setPageBean(newLastId, request.pageSize, 0, size);
        }
        return json(BaseResponse.CODE_SUCCESS, "获取成功", response);
    }

    public String listOfAgentMy(ActionBean bean) {

        WorkOrderBean.WorkOrderListRequest request = requst2Bean(bean.body, WorkOrderBean.WorkOrderListRequest.class);
        WorkOrderBean.WorkOrderListResponse response = new WorkOrderBean.WorkOrderListResponse();

        List<CheckParamsKit.Entry<String, String>> entries = checkParams(request);
        if (entries != null) {
            return json(BaseResponse.CODE_PARAM_ERROR, entries, response);
        }

        WorkOrder search = new WorkOrder();
        search.addressee_uuid = bean.managerUuid;
        search.sender_uuid = bean.accountUuid;
        search.type = WorkOrder.TYPE_MANAGER;
        search.page = setPage(request.lastId, request.pageNum, request.pageSize);
        List<WorkOrder> orderList = workOrderDao.findPage(search);
        List<WorkOrderBean.WorkOrderData> dataList = new ArrayList<>();
        long newLastId = 0;
        if (orderList != null && !orderList.isEmpty()) {
            AgentJobBean agent = agentJobClient.getAgent(bean.managerUuid, Long.valueOf(bean.userId));
            String name = "";
            if(agent!=null){
                name = agent.name;
            }
            for (WorkOrder order : orderList) {
                WorkOrderBean.WorkOrderData data = toData(order,false,false);
                data.senderName = name;
                newLastId = order.id;
                dataList.add(data);
            }
        }
        response.data = dataList;
        int size = dataList.size();
        if (StringKit.isInteger(request.pageNum)) {
            int total = workOrderDao.findCount(search);
            response.page = setPageBean(request.pageNum, request.pageSize, total, size);
        } else if (StringKit.isInteger(request.lastId)) {
            response.page = setPageBean(newLastId, request.pageSize, 0, size);
        }

        return json(BaseResponse.CODE_SUCCESS, "获取成功", response);
    }


    /**
     * 创建工单
     *
     * @param actionBean     工单
     * @return json
     * @access public
     */
    public String addWork(ActionBean actionBean) {
        WorkOrderBean.addWork request = requst2Bean(actionBean.body,WorkOrderBean.addWork.class);

        BaseResponse response = new BaseResponse();

        //判空
        List<CheckParamsKit.Entry<String, String>> entries = checkParams(request);
        if (entries != null) {
            return json(BaseResponse.CODE_PARAM_ERROR, entries, response);
        }

        //获取当前时间戳(毫秒值)
        long date = TimeKit.currentTimeMillis();

        //工单号
        String code = getStringRandom(6);

        //赋值
        WorkOrder workOrder = new WorkOrder();
        workOrder.title = request.title;
        workOrder.content = request.content;
        workOrder.category_id = request.categoryId;
        workOrder.category_extra_name = request.categoryExtraName;
        workOrder.addressee_uuid = actionBean.managerUuid;
        workOrder.sender_uuid = actionBean.accountUuid;
        workOrder.type = WorkOrder.TYPE_MANAGER;
        workOrder.close_status = WorkOrder.STATUS_CLOSE_NO;
        workOrder.solve_status = WorkOrder.STATUS_SOLVE_WEIFANKUI;
        workOrder.handle_status = WorkOrder.STATUS_HANDLE_WAITING;
//        workOrder.wo_num = code;
        workOrder.created_at = date;
        workOrder.updated_at = date;
        workOrder.state = 1;

        //入库
        int add_res = workOrderDao.insert(workOrder);
        if (add_res == 1) {
            return json(BaseResponse.CODE_SUCCESS, "工单创建成功", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "工单创建失败", response);
        }

    }

    public String categoryList(ActionBean bean){
        BaseRequest request = requst2Bean(bean.body,BaseRequest.class);
        WorkOrderBean.WOCategoryListResponse response = new WorkOrderBean.WOCategoryListResponse();
        List<CheckParamsKit.Entry<String, String>> entries = checkParams(request);
        if (entries != null) {
            return json(BaseResponse.CODE_PARAM_ERROR, entries, response);
        }

        List<WorkOrderCategory> list = workOrderDao.findCategoryList();
        List<WorkOrderBean.WOCategoryData> dataList = new ArrayList<>();
        if(list!=null && !list.isEmpty()){
            for (WorkOrderCategory category : list) {
                WorkOrderBean.WOCategoryData data = new WorkOrderBean.WOCategoryData();
                data.id = category.id;
                data.name = category.name;
                data.writable = category.writable;
                dataList.add(data);
            }
        }
        response.data = dataList;
        return json(BaseResponse.CODE_SUCCESS,"获取成功",response);
    }

    public String reply(ActionBean bean){
        WorkOrderBean.WorkOrderReplyRequest request = requst2Bean(bean.body, WorkOrderBean.WorkOrderReplyRequest.class);
        BaseResponse response = new BaseResponse();
        List<CheckParamsKit.Entry<String, String>> entries = checkParams(request);
        if (entries != null) {
            return json(BaseResponse.CODE_PARAM_ERROR, entries, response);
        }
        WorkOrder workOrder = workOrderDao.findOne(Long.valueOf(request.woId));
        if(workOrder!=null && workOrder.addressee_uuid.equals(bean.managerUuid)){

            if (workOrder.close_status!=WorkOrder.STATUS_CLOSED){
                return json(BaseResponse.CODE_FAILURE,"已关闭",response);
            }

            WorkOrderReply reply = new WorkOrderReply();
            reply.content = request.content;
            reply.work_order_id = workOrder.id;
            reply.replier_uuid = bean.accountUuid;
            reply.created_at = TimeKit.currentTimeMillis();
            if(workOrderDao.addReply(reply)>0){
                return json(BaseResponse.CODE_SUCCESS,"回复成功",response);
            }else{
                return json(BaseResponse.CODE_FAILURE,"回复失败",response);
            }
        }
        return json(BaseResponse.CODE_FAILURE,"回复失败",response);
    }

    public String score(ActionBean bean){

        WorkOrderBean.WorkOrderCommentRequest request = requst2Bean(bean.body, WorkOrderBean.WorkOrderCommentRequest.class);
        BaseResponse response = new BaseResponse();
        List<CheckParamsKit.Entry<String, String>> entries = checkParams(request);
        if (entries != null) {
            return json(BaseResponse.CODE_PARAM_ERROR, entries, response);
        }
        WorkOrder workOrder = workOrderDao.findOne(Long.valueOf(request.woId));


        if(workOrder!=null && workOrder.addressee_uuid.equals(bean.managerUuid)){
            if (workOrder.solve_status != WorkOrder.STATUS_SOLVE_WEIFANKUI){
                return json(BaseResponse.CODE_FAILURE,"已反馈",response);
            }
            if(workOrder.handle_status != WorkOrder.STATUS_HANDLE_DONE){
                return json(BaseResponse.CODE_FAILURE,"工单未处理",response);
            }

            WorkOrder update = new WorkOrder();
            update.id = workOrder.id;
            update.solve_status = Integer.valueOf(request.solveStatus);
            update.updated_at = TimeKit.currentTimeMillis();
            if(workOrderDao.updateSolveStatus(update)>0){
                return json(BaseResponse.CODE_SUCCESS,"反馈成功",response);
            }else{
                return json(BaseResponse.CODE_FAILURE,"反馈失败",response);
            }
        }
        return json(BaseResponse.CODE_FAILURE,"反馈失败",response);

    }

    public String detail(ActionBean bean){
        WorkOrderBean.WorkOrderGetRequest request = requst2Bean(bean.body, WorkOrderBean.WorkOrderGetRequest.class);
        WorkOrderBean.WorkOrderGetResponse response = new WorkOrderBean.WorkOrderGetResponse();
        List<CheckParamsKit.Entry<String, String>> entries = checkParams(request);
        if (entries != null) {
            return json(BaseResponse.CODE_PARAM_ERROR, entries, response);
        }
        WorkOrder workOrder = workOrderDao.findOne(Long.valueOf(request.woId));
        if(workOrder!=null && workOrder.addressee_uuid.equals(bean.managerUuid)){

            WorkOrderBean.WorkOrderData data = toData(workOrder, true, true);
            data.orderStatusTxt = WorkOrder.getHandle(workOrder.handle_status);

            data.replyList = new ArrayList<>();

            List<WorkOrderReply> replyList = workOrderDao.findReplyList(workOrder.id);
            if(replyList!=null && !replyList.isEmpty()){
                for (WorkOrderReply reply : replyList) {
                    WorkOrderBean.WOReplyData replyData = new WorkOrderBean.WOReplyData();
                    replyData.content = reply.content;
                    replyData.replyTimeTxt = TimeKit.format("yyyy-MM-dd HH:mm",reply.created_at);
                    if(reply.replier_uuid.equals(bean.managerUuid)){
                        replyData.replierName = "管理员";
                    }else{
                        AgentJobBean agent = getAgent(bean.managerUuid, reply.replier_uuid);
                        if(agent!=null){
                            replyData.replierName = agent.name;
                        }else{
                            replyData.replierName = "提交者";
                        }
                    }

                    data.replyList.add(replyData);
                }
            }
            if(workOrder.solve_status!=WorkOrder.STATUS_SOLVE_WEIFANKUI){
                data.orderResult = "问题"+data.orderResult;
                if(bean.userType==3){
                    data.orderResult = "提交人反馈："+data.orderResult;
                }
                data.ratable = 0;
            }else{
                if(bean.userType==4){
                    data.ratable = 1;
                }else{
                    data.ratable = 0;
                }
            }

            response.data = data;
            return json(BaseResponse.CODE_SUCCESS,"获取成功",response);
        }
        return json(BaseResponse.CODE_FAILURE,"获取失败",response);
    }



    private WorkOrderBean.WorkOrderData toData(WorkOrder order, boolean isGetUser,boolean isGetCategory) {
        WorkOrderBean.WorkOrderData data = new WorkOrderBean.WorkOrderData();
        data.id = order.id;
        data.woNum = order.wo_num;
        data.title = order.title;
        data.content = order.content;
        data.submitTimeTxt = TimeKit.format("yyyy-MM-dd HH:mm", order.created_at);
        data.orderResult = WorkOrder.getResult(order.solve_status);
        if (order.close_status == WorkOrder.STATUS_CLOSED){
            data.orderStatusTxt = "已关闭";
            data.orderStatus = 4;
        }else{
            data.orderStatusTxt = WorkOrder.getHandle(order.handle_status);
            data.orderStatus = order.handle_status;
        }
        if (isGetUser) {
            AgentJobBean agent = getAgent(order.addressee_uuid, order.sender_uuid);
            if (agent != null) {
                if (agent.channel_id > 0) {
                    ChannelBean channel = channelClient.getChannel(String.valueOf(agent.channel_id));
                    if (channel != null) {
                        data.channelName = channel.name;
                    }else{
                        data.channelName = "";
                    }
                }
                data.senderName = agent.name;
            }
        }
        if(isGetCategory){
            WorkOrderCategory category = workOrderCategoryDao.findOne(order.category_id);
            if(category!=null){
                data.categoryName = category.name;
            }
            if(!StringKit.isEmpty(order.category_extra_name)){
                if(data.categoryName!=null){
                    data.categoryName += "[" +order.category_extra_name+"]";
                }else{
                    data.categoryName = order.category_extra_name;
                }
            }
        }
        return data;
    }



    private AgentJobBean getAgent(String managerUuid, String userUuid) {
        AccountBean accountBean = accountClient.findByUuid(userUuid);
        if (accountBean != null) {
            return agentJobClient.getAgent(managerUuid, Long.valueOf(accountBean.userId));
        }
        return null;
    }




    /**
     * 生成随机数字和字母
     *
     * @param length
     * @return
     */
    public String getStringRandom(int length) {
        String val = "";
        Random random = new Random();
        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

}
