package com.inschos.message.data.dao;

import com.inschos.message.data.mapper.MsgInboxMapper;
import com.inschos.message.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消息 收件处理数据访问对象（按功能划分DAO）
 * 收件箱列表(findMsgRecList)，
 * 某一分类消息列表(findMsgRecListByType)
 * 发件箱列表(findMsgSysList),
 * 用户未接收信息查询(findMsgSysALL),
 * 消息 详情（findMsgInfo），
 * 删除/读取消息 （updateMsgRec）
 * TODO  在DAO里传对象，需要判空！！！
 */
@Component
public class MsgInboxDAO {
    @Autowired
    private MsgInboxMapper msgInboxMapper;

    /**
     * 收件箱列表查询
     *
     * @param msgRec
     * @return list
     * @access public
     */
    public List<MsgTypeLists> findMsgRecList(MsgRec msgRec) {
        return msgInboxMapper.findMsgRecList(msgRec);
    }

    /**
     * 收件箱列表查询-按消息分类
     *
     * @param msgRec
     * @return list
     * @access public
     */
    public List<MsgRec> findMsgRecListByType(MsgRec msgRec) {
        return msgInboxMapper.findMsgRecListByType(msgRec);
    }

    /**
     * 收件箱列表查询-按parent_id
     *
     * @param msgRec
     * @return list
     * @access public
     */
    public List<MsgRec> findMsgRecListByParent(MsgRec msgRec) {
        return msgInboxMapper.findMsgRecListByParent(msgRec);
    }

    /**
     * 收取消息 （系统把消息 同步到用户收件箱,同时修改系统发件表的状态）
     *
     * @param msgRec
     * @return int
     * @access public
     */
    public int insertMsgRec(MsgRec msgRec) {
        return msgInboxMapper.insertMsgRec(msgRec);
    }

    /**
     * 收取消息 （系统把消息 同步到用户收件箱,同时修改系统发件表的状态）
     *
     * @param msgSys
     * @return int
     * @access public
     */
    public int updateMsgSysStatus(MsgSys msgSys) {
        return msgInboxMapper.updateMsgSysStatus(msgSys);
    }

    /**
     * 用户未收件(用户登录之后，查询系统收件箱，用户为读取的消息)
     *
     * @param msgRec
     * @return list
     * @access public
     */
    public List<MsgSys> findUserMsgRes(MsgRec msgRec) {
        return msgInboxMapper.findUserMsgRes(msgRec);
    }

    /**
     * 发件箱列表查询
     *
     * @param msgSys
     * @return list
     * @access public
     */
    public List<MsgSys> findMsgSysList(MsgSys msgSys) {
        return msgInboxMapper.findMsgSysList(msgSys);
    }

    /**
     * 发件箱列表查询-按消息分类
     *
     * @param msgSys
     * @return list
     * @access public
     */
    public List<MsgSys> findMsgSysListByType(MsgSys msgSys) {
        return msgInboxMapper.findMsgSysListByType(msgSys);
    }

    /**
     * 收件箱列表查询-按消息分类
     *
     * @param msgSys
     * @return list
     * @access public
     */
    public List<MsgSys> findMsgSysListByParent(MsgSys msgSys) {
        return msgInboxMapper.findMsgSysListByParent(msgSys);
    }

    /**
     * 发件箱列表查询
     *
     * @param msgSys
     * @return msgSys
     * @access public
     */
    public MsgSys findMsgSysInfo(MsgSys msgSys) {
        return msgInboxMapper.findMsgSysInfo(msgSys);
    }

    /**
     * 消息 详情查询
     *
     * @param msgRec
     * @return msgRec
     * @access public
     */
    public MsgRec findMsgInfo(MsgRec msgRec) {
        return msgInboxMapper.findMsgInfo(msgRec);
    }

    /**
     *
     * @param msgSys
     * @return
     */
    public List<MsgTo> findMsgTo(MsgSys msgSys){
        return msgInboxMapper.findMsgTo(msgSys);
    }

    /**
     * 删除/读取消息 （update）
     *
     * @param msgUpdate
     * @return int
     * @access public
     */
    public int updateMsgRecStatus(MsgUpdate msgUpdate) {
        return msgInboxMapper.updateMsgRecStatus(msgUpdate);
    }

    /**
     * 删除/读取消息 （update）
     *
     * @param msgUpdate
     * @return int
     * @access public
     */
    public int updateMsgRecState(MsgUpdate msgUpdate) {
        return msgInboxMapper.updateMsgRecState(msgUpdate);
    }


}
