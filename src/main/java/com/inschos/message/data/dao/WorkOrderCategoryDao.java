package com.inschos.message.data.dao;

import com.inschos.message.model.WorkOrderCategory;

/**
 * Created by IceAnt on 2018/5/21.
 */
public interface WorkOrderCategoryDao {

    WorkOrderCategory findOne(long id);
}
