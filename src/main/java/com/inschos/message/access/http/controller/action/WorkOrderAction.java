package com.inschos.message.access.http.controller.action;

import com.inschos.message.access.http.controller.bean.ActionBean;
import com.inschos.message.access.http.controller.bean.BaseResponse;
import com.inschos.message.access.http.controller.bean.WorkOrderBean;
import com.inschos.message.access.rpc.bean.AccountBean;
import com.inschos.message.access.rpc.bean.AgentJobBean;
import com.inschos.message.access.rpc.bean.ChannelBean;
import com.inschos.message.access.rpc.client.AccountClient;
import com.inschos.message.access.rpc.client.AgentJobClient;
import com.inschos.message.access.rpc.client.ChannelClient;
import com.inschos.message.annotation.CheckParamsKit;
import com.inschos.message.assist.kit.JsonKit;
import com.inschos.message.assist.kit.StringKit;
import com.inschos.message.assist.kit.TimeKit;
import com.inschos.message.data.dao.WorkOrderCategoryDao;
import com.inschos.message.data.dao.WorkOrderDao;
import com.inschos.message.model.WorkOrder;
import com.inschos.message.model.WorkOrderCategory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        long date = new Date().getTime();

        //赋值
        WorkOrder workOrder = new WorkOrder();
        workOrder.title = request.title;
        workOrder.content = request.content;
        workOrder.category_id = request.category_id;
        workOrder.category_extra_name = request.category_extra_name;
        workOrder.addressee_uuid = actionBean.managerUuid;
        workOrder.sender_uuid = actionBean.accountUuid;
        workOrder.type = request.type; //工单类型  1 对业管的  2 业管对天眼的
        workOrder.close_status = request.close_status;
        workOrder.solve_status = request.solve_status;
        workOrder.handle_status = request.handle_status;
        workOrder.created_at = date;
        workOrder.updated_at = date;
        workOrder.state = request.state;

        //入库
        int add_res = workOrderDao.insert(workOrder);
        if (add_res == 1) {
            return json(BaseResponse.CODE_SUCCESS, "工单创建成功", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "工单创建失败", response);
        }

    }


    private WorkOrderBean.WorkOrderData toData(WorkOrder order, boolean isGetUser,boolean isGetCategory) {
        WorkOrderBean.WorkOrderData data = new WorkOrderBean.WorkOrderData();
        data.id = order.id;
        data.woNum = order.wo_num;
        data.title = order.title;
        data.content = order.content;
        data.submitTime = TimeKit.format("yyyy-MM-dd HH:mm", order.created_at);
        data.orderResult = WorkOrder.getResult(order.solve_status);
        if (isGetUser) {
            AgentJobBean agent = getAgent(order.addressee_uuid, order.sender_uuid);
            if (agent != null) {
                if (agent.channel_id > 0) {
                    ChannelBean channel = channelClient.getChannel(String.valueOf(agent.channel_id));
                    if (channel != null) {
                        data.channelName = channel.name;
                    }
                }
                data.senderName = agent.name;
            }
        }
        if(isGetCategory){
            WorkOrderCategory category = workOrderCategoryDao.findOne(order.category_id);
            if(category!=null){
                data.categoryName = category.name;
                if(category.writable==WorkOrderCategory.WRITABLE_OK && !StringKit.isEmpty(category.write_name)){
                    data.categoryName = data.categoryName+"["+category.write_name+"]";
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

}
