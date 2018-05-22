package com.inschos.message.data.dao.impl;

import com.inschos.message.data.dao.BaseDao;
import com.inschos.message.data.dao.WorkOrderDao;
import com.inschos.message.data.mapper.WorkOrderCategoryMapper;
import com.inschos.message.data.mapper.WorkOrderMapper;
import com.inschos.message.data.mapper.WorkOrderReplyMapper;
import com.inschos.message.model.WorkOrder;
import com.inschos.message.model.WorkOrderCategory;
import com.inschos.message.model.WorkOrderReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IceAnt on 2018/5/21.
 */
@Repository
public class WorkOrderDaoImpl extends BaseDao implements WorkOrderDao {

    @Autowired
    private WorkOrderMapper workOrderMapper;
    @Autowired
    private WorkOrderCategoryMapper workOrderCategoryMapper;
    @Autowired
    private WorkOrderReplyMapper workOrderReplyMapper;

    @Override
    public WorkOrder findOne(long id) {
        return workOrderMapper.selectOne(id);
    }

    @Override
    public List<WorkOrder> findPage(WorkOrder search) {
        return search!=null?workOrderMapper.selectPage(search):null;
    }

    @Override
    public int findCount(WorkOrder search) {
        return search!=null?workOrderMapper.selectCount(search):0;
    }

    @Override
    public List<WorkOrderCategory> findCategoryList() {
        return workOrderCategoryMapper.selectAllValid();
    }

    @Override
    public int addReply(WorkOrderReply reply) {
        int flag = 0;
        flag = workOrderReplyMapper.insert(reply);
        if(flag>0){
            WorkOrder workOrder = new WorkOrder();
            workOrder.id = reply.work_order_id;
            workOrder.close_status = WorkOrder.STATUS_CLOSED;
            workOrder.handle_status = WorkOrder.STATUS_HANDLE_DONE;
            workOrder.updated_at = reply.created_at;
            flag = workOrderMapper.updateHandleCloseStatus(workOrder);
            if(flag<=0){
                rollBack();
            }
        }
        return flag;
    }

    @Override
    public int updateSolveStatus(WorkOrder update) {
        return workOrderMapper.updateSolveStatus(update);
    }

    @Override
    public List<WorkOrderReply> findReplyList(long woId) {
        return workOrderReplyMapper.selectAll(woId);
    }


}
