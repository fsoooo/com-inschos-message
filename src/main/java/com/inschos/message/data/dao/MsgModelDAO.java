package com.inschos.message.data.dao;

import com.inschos.message.data.mapper.MsgModelMapper;
import com.inschos.message.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消息 模板处理数据访问对象（按功能划分DAO）
 * 模板添加（addModel），模板列表查询(findModelList)，模板详情查询(findModelInfo)，模板更新(updateModel)
 */
@Component
public class MsgModelDAO {
    @Autowired
    private MsgModelMapper msgModelMapper;

    /**
     * 添加消息 模板
     *
     * @param modelName       模板名称（不能一样）
     * @param modelContent    模板内容
     * @param modelType       模板类型
     * @param createdUser     创建者姓名
     * @param createdUserType 创建者类型
     * @return json
     * @access public
     */
    public int addMsgModel(MsgModel msgModel) {
        return msgModelMapper.addMsgModel(msgModel);
    }

    /**
     * 获取模板是否重复
     *
     * @param msgModel
     * @return
     */
    public MsgModel findMsgModelRepeat(MsgModel msgModel) {
        return msgModelMapper.findMsgModelRepeat(msgModel);
    }

    /**
     * 消息 模板列表
     *
     * @param pageNum     当前页码 ，可不传，默认为1
     * @param lastId      上一页最大id ，可不传，默认为
     * @param limit       每页显示行数，可不传，默认为
     * @param modelStatus 模板状态（审核通过0/未通过1/已删除2）
     * @param modelSype   模板类型
     * @return json
     * @access public
     */
    public List<MsgModel> findMsgModelList(MsgModelList msgModelList) {
        return msgModelMapper.findMsgModelList(msgModelList);
    }

    /**
     * 模板详情查询
     *
     * @param modelCode|模板代号
     * @return mixed
     * @access public
     */
    public MsgModel findMsgModelInfo(MsgModel msgModel) {
        return msgModelMapper.findMsgModelInfo(msgModel);
    }

    /**
     * 消息 模板操作（审核、删除）
     *
     * @param modelCode 模板代码
     * @param status    模板状态（审核通过1，删除2）
     * @param modelType 模板类型
     * @param userId    操作人id
     * @param userType  操作人类型（只有业管可以审核和删除）
     * @return json
     * @access public
     */
    public int updateMsgModel(MsgModelUpdate msgModelUpdate) {
        return msgModelMapper.updateMsgModel(msgModelUpdate);
    }

    /**
     * 消息 模板更新
     *
     * @param modelCode 模板代码
     * @param status    模板状态（审核通过1，删除2）
     * @param userId    操作人id
     * @param userType  操作人类型（只有业管可以审核和删除）
     * @return json
     * @access public
     */
    public int updateMsgModelStatus(MsgModelUpdate msgModelUpdate) {
        return msgModelMapper.updateMsgModelStatus(msgModelUpdate);
    }

    /**
     * 消息 模板更新
     *
     * @param modelCode 模板代码
     * @param modelType 模板类型
     * @param userId    操作人id
     * @param userType  操作人类型（只有业管可以审核和删除）
     * @return json
     * @access public
     */
    public int updateMsgModelType(MsgModelUpdate msgModelUpdate) {
        return msgModelMapper.updateMsgModelType(msgModelUpdate);
    }

}
