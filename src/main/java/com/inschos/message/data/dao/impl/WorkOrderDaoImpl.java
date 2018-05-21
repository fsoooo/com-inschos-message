package com.inschos.message.data.dao.impl;

import com.inschos.message.data.dao.WorkOrderDao;
import com.inschos.message.data.mapper.WorkOrderMapper;
import com.inschos.message.model.MsgModel;
import com.inschos.message.model.WorkOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IceAnt on 2018/5/21.
 */
@Repository
public class WorkOrderDaoImpl implements WorkOrderDao {

    @Autowired
    private WorkOrderMapper workOrderMapper;

    @Override
    public List<WorkOrder> findPage(WorkOrder search) {
        return search!=null?workOrderMapper.selectPage(search):null;
    }

    @Override
    public int findCount(WorkOrder search) {
        return search!=null?workOrderMapper.selectCount(search):0;
    }

    @Override
    public int insert(WorkOrder workOrder){
        return workOrderMapper.insert(workOrder);
    }



}
