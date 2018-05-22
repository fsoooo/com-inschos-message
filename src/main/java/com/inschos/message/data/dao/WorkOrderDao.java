package com.inschos.message.data.dao;

import com.inschos.message.model.WorkOrder;
import com.inschos.message.model.WorkOrderCategory;
import com.inschos.message.model.WorkOrderReply;

import java.util.List;

/**
 * Created by IceAnt on 2018/5/21.
 */
public interface WorkOrderDao {

    WorkOrder findOne(long id);

    /** 通过type 和 发送 、接收人 分页查询   */
    List<WorkOrder> findPage(WorkOrder search);

    /** 通过type 和 发送 、接收人 查询记录总数   */
    int findCount(WorkOrder search);

    List<WorkOrderCategory> findCategoryList();

    int addReply(WorkOrderReply reply);

    int updateSolveStatus(WorkOrder update);

    List<WorkOrderReply> findReplyList(long woId);

}
