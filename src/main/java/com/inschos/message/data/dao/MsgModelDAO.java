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
     * @params modelName       模板名称（不能一样）
     * @params modelContent    模板内容
     * @params modelType       模板类型
     * @params createdUser     创建者姓名
     * @params createdUserType 创建者类型
     * @return json
     * @access public
     */
    public int addMsgModel(MsgModel msgModel) {
        return msgModelMapper.addMsgModel(msgModel);
    }

    /**
     * 获取模板是否重复
     *
     * @params msgModel
     * @return
     */
    public MsgModel findMsgModelRepeat(MsgModel msgModel) {
        return msgModelMapper.findMsgModelRepeat(msgModel);
    }

    /**
     * 消息 模板列表
     *
     * @params pageNum     当前页码 ，可不传，默认为1
     * @params lastId      上一页最大id ，可不传，默认为
     * @params limit       每页显示行数，可不传，默认为
     * @params modelStatus 模板状态（审核通过0/未通过1/已删除2）
     * @params modelSype   模板类型
     * @return json
     * @access public
     */
    public List<MsgModel> findMsgModelList(MsgModelList msgModelList) {
        return msgModelMapper.findMsgModelList(msgModelList);
    }

    /**
     * 模板详情查询
     *
     * @params modelCode|模板代号
     * @return mixed
     * @access public
     */
    public MsgModel findMsgModelInfo(MsgModel msgModel) {
        return msgModelMapper.findMsgModelInfo(msgModel);
    }

    /**
     * 消息 模板操作（审核、删除）
     *
     * @params modelCode 模板代码
     * @params status    模板状态（审核通过1，删除2）
     * @params modelType 模板类型
     * @params userId    操作人id
     * @params userType  操作人类型（只有业管可以审核和删除）
     * @return json
     * @access public
     */
    public int updateMsgModel(MsgModelUpdate msgModelUpdate) {
        return msgModelMapper.updateMsgModel(msgModelUpdate);
    }

    /**
     * 消息 模板更新
     *
     * @params modelCode 模板代码
     * @params status    模板状态（审核通过1，删除2）
     * @params userId    操作人id
     * @params userType  操作人类型（只有业管可以审核和删除）
     * @return json
     * @access public
     */
    public int updateMsgModelStatus(MsgModelUpdate msgModelUpdate) {
        return msgModelMapper.updateMsgModelStatus(msgModelUpdate);
    }

    /**
     * 消息 模板更新
     *
     * @params modelCode 模板代码
     * @params modelType 模板类型
     * @params userId    操作人id
     * @params userType  操作人类型（只有业管可以审核和删除）
     * @return json
     * @access public
     */
    public int updateMsgModelType(MsgModelUpdate msgModelUpdate) {
        return msgModelMapper.updateMsgModelType(msgModelUpdate);
    }

}
