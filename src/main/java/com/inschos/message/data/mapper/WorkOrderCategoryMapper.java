package com.inschos.message.data.mapper;

import com.inschos.message.model.WorkOrderCategory;

/**
 * Created by IceAnt on 2018/5/21.
 */
public interface WorkOrderCategoryMapper {

    int insert(WorkOrderCategory record);

    int update(WorkOrderCategory record);

    WorkOrderCategory selectOne(long id);
}
