package com.inschos.message.data.dao;

import com.inschos.message.model.WorkOrder;

import java.util.List;

/**
 * Created by IceAnt on 2018/5/21.
 */
public interface WorkOrderDao {

    /** 通过type 和 发送 、接收人 分页查询   */
    List<WorkOrder> findPage(WorkOrder search);

    /** 通过type 和 发送 、接收人 查询记录总数   */
    int findCount(WorkOrder search);

    /** 新建工单   */
    int insert(WorkOrder workOrder);//定义返回数据类型-整型-影响数据库行数


}
