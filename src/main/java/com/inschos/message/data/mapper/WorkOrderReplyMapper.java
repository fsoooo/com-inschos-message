package com.inschos.message.data.mapper;

import com.inschos.message.model.WorkOrderReply;

import java.util.List;

/**
 * Created by IceAnt on 2018/5/21.
 */
public interface WorkOrderReplyMapper {

    int insert(WorkOrderReply record);

    int update(WorkOrderReply record);

    WorkOrderReply selectOne(long id);

    List<WorkOrderReply> selectAll(long woId);
}
