package com.inschos.message.data.mapper;

import com.inschos.message.model.WorkOrder;

import java.util.List;

/**
 * Created by IceAnt on 2018/5/21.
 */
public interface WorkOrderMapper {

    int insert(WorkOrder record);

    int update(WorkOrder record);

    WorkOrder selectOne(long id);

    /** 通过type 和 发送 、接收人 分页查询 |  可选查询值 handler close  */
    List<WorkOrder> selectPage(WorkOrder search);

    /** 通过type 和 发送 、接收人 查询记录总数   */
    int selectCount(WorkOrder search);

}
