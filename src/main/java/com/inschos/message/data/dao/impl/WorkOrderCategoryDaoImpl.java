package com.inschos.message.data.dao.impl;

import com.inschos.message.data.dao.WorkOrderCategoryDao;
import com.inschos.message.data.mapper.WorkOrderCategoryMapper;
import com.inschos.message.model.WorkOrderCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by IceAnt on 2018/5/21.
 */
@Repository
public class WorkOrderCategoryDaoImpl implements WorkOrderCategoryDao {

    @Autowired
    private WorkOrderCategoryMapper workOrderCategoryMapper;

    @Override
    public WorkOrderCategory findOne(long id) {
        return workOrderCategoryMapper.selectOne(id);
    }
}
