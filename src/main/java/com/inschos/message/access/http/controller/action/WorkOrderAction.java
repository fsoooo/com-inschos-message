package com.inschos.message.access.http.controller.action;

import com.inschos.message.access.http.controller.bean.ActionBean;
import com.inschos.message.access.http.controller.bean.BaseResponse;
import com.inschos.message.access.http.controller.bean.MsgModelBean;
import com.inschos.message.access.http.controller.bean.WorkOrderBean;
import com.inschos.message.assist.kit.JsonKit;
import com.inschos.message.model.MsgModel;
import com.inschos.message.model.WorkOrder;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class WorkOrderAction {
    private static final Logger logger = Logger.getLogger(MsgModelAction.class);


    /**
     * 创建工单
     *
     * @param name        工单标题
     * @param classify     类型
     * @param content      内容
     * @return json
     * @access public
     */


    public String addWork(ActionBean actionBean) {
        WorkOrderBean.addWork request = JsonKit.json2Bean(actionBean.body,WorkOrderBean.addWork.class);

//        BaseResponse response = new BaseResponse();
        //判空
//        if (request == null) {
//            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
//        }

        //获取当前时间戳(毫秒值)
        long date = new Date().getTime();

        //赋值
        WorkOrder workOrder = new WorkOrder();
        workOrder.title = request.title;
        workOrder.content = request.content;
        workOrder.category_id = request.category_id;
//        workOrder.addressee_uuid = addressee_uuid;
//        workOrder.sender_uuid = sender_uuid;
        workOrder.type = request.type; //工单类型  1 对业管的  2 业管对天眼的
        workOrder.close_status = request.close_status;
        workOrder.solve_status = request.solve_status;
        workOrder.handle_status = request.handle_status;
        workOrder.created_at = date;
        workOrder.updated_at = date;

        //判断模板是否重复
//        MsgModel msgModelRepeat = msgModelDAO.getMsgModelRepeat(msgModel);
        

        return "55858";
    }
}
