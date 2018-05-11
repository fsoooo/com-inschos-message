package com.inschos.message.data.dao;

import com.inschos.message.data.mapper.MsgModelMapper;
import com.inschos.message.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 站内信模板处理数据访问对象（按功能划分DAO）
 * 模板添加（addModel），模板列表查询(getModelList)，模板详情查询(getModelInfo)，模板更新(updateModel)
 */
@Component
public class MsgModelDAO {
    @Autowired
    private MsgModelMapper msgModelMapper;

    /**
     * 添加站内信模板
     * @access public
     * @param model_code|模板名称  以一定规则生成，不重复
     * @param model_name|模板名称
     * @param model_content|模板详细内容
     * @param status|审核状态:默认为0审核中，1审核通过，2审核失败
     * @param created_user|创建用户id
     * @param created_user_type|创建用户type:个人用户1/企业用户2/管理员等3
     * @return mixed
     *
     */
    public int addMsgModel(MsgModel msgModel){
        return msgModelMapper.addMsgModel(msgModel);
    }

    /**
     * 获取模板是否重复
     * @param msgModel
     * @return
     */
    public MsgModel getMsgModelRepeat(MsgModel msgModel){
        return msgModelMapper.getMsgModelRepeat(msgModel);
    }

    /**
     * 模板列表查询
     * @access public
     * @param page|分页信息
     * @return mixed
     *
     */
    public List<MsgModel> getMsgModelList(MsgModelList msgModelList){
        return msgModelMapper.getMsgModelList(msgModelList);
    }

    /**
     * 模板详情查询
     * @access public
     * @param model_code|模板代号
     * @return mixed
     *
     */
    public MsgModel getMsgModelInfo(String model_code){
        return msgModelMapper.getMsgModelInfo(model_code);
    }

    /**
     * 更新站内信模板状态（update）
     * @access public
     * @param model_code|模板代号
     * @param update_data|更新数据
     * ['deleted_at'=>time()] 删除
     * ['status'=>'1'] 审核通过
     * @return mixed
     *
     */
    public int updateMsgModel(MsgModelUpdate msgModelUpdate){
        return msgModelMapper.updateMsgModel(msgModelUpdate);
    }

}
