package com.inschos.message.data.mapper;

import com.inschos.message.model.WorkOrder;

/**
 * Created by IceAnt on 2018/5/21.
 */
public interface WorkOrderMapper {

    int insert(WorkOrder record);

    int update(WorkOrder record);

    WorkOrder selectOne(long id);
}
