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
     * @params msgRec
     * @return list
     * @access public
     */
    public List<MsgTypeLists> findMsgRecList(MsgRec msgRec) {
        return msgInboxMapper.findMsgRecList(msgRec);
    }

    /**
     * 获取消息总数
     * @param msgRec
     * @return
     */
    public MsgCount findMsgRecCount(MsgRec msgRec){
        return msgInboxMapper.findMsgRecCount(msgRec);
    }

    /**
     * 获取消息总数
     * @param msgSys
     * @return
     */
    public MsgCount findMsgSysCount(MsgSys msgSys){
        return msgInboxMapper.findMsgSysCount(msgSys);
    }

    /**
     * 收件箱列表查询-按消息分类
     *
     * @params msgRec
     * @return list
     * @access public
     */
    public List<MsgRec> findMsgRecListByType(MsgRec msgRec) {
        return msgInboxMapper.findMsgRecListByType(msgRec);
    }

    /**
     * 收取消息 （系统把消息 同步到用户收件箱,同时修改系统发件表的状态）
     *
     * @params msgRec
     * @return int
     * @access public
     */
    public int insertMsgRec(MsgRec msgRec) {
        return msgInboxMapper.insertMsgRec(msgRec);
    }

    /**
     * 收取消息 （系统把消息 同步到用户收件箱,同时修改系统发件表的状态）
     *
     * @params msgSys
     * @return int
     * @access public
     */
    public int updateMsgSysStatus(MsgSys msgSys) {
        return msgInboxMapper.updateMsgSysStatus(msgSys);
    }

    public int updateMsgToRecord(MsgSys msgSys) {
        return msgInboxMapper.updateMsgToRecord(msgSys);
    }


    /**
     * 用户未收件(用户登录之后，查询系统收件箱，用户为读取的消息)
     *
     * @params msgRec
     * @return list
     * @access public
     */
    public List<MsgSys> findUserMsgRes(MsgRec msgRec) {
        return msgInboxMapper.findUserMsgRec(msgRec);
    }

    /**
     * 从消息发送对象表获取未读消息
     *
     * @params msgRec
     * @return list
     * @access public
     */
    public List<MsgSys> findMsgToRecord(MsgRec msgRec){
        return msgInboxMapper.findMsgToRecord(msgRec);
    }

    public MsgSys findMsgSysRes(MsgSys msgSys){
        return msgInboxMapper.findMsgSysRec(msgSys);
    }

    /**
     * 发件箱列表查询
     *
     * @params msgSys
     * @return list
     * @access public
     */
    public List<MsgTypeLists> findMsgSysList(MsgSys msgSys) {
        return msgInboxMapper.findMsgSysList(msgSys);
    }

    /**
     * 发件箱列表查询-按消息分类
     *
     * @params msgSys
     * @return list
     * @access public
     */
    public List<MsgSys> findMsgSysListByType(MsgSys msgSys) {
        return msgInboxMapper.findMsgSysListByType(msgSys);
    }

    /**
     * 发件箱列表查询
     *
     * @params msgSys
     * @return msgSys
     * @access public
     */
    public MsgSys findMsgSysInfo(MsgSys msgSys) {
        return msgInboxMapper.findMsgSysInfo(msgSys);
    }

    /**
     * 消息 详情查询
     *
     * @params msgRec
     * @return msgRec
     * @access public
     */
    public MsgRec findMsgInfo(MsgRec msgRec) {
        return msgInboxMapper.findMsgInfo(msgRec);
    }

    /**
     *
     * @params msgSys
     * @return
     */
    public List<MsgTo> findMsgTo(MsgSys msgSys){
        return msgInboxMapper.findMsgTo(msgSys);
    }

    /**
     * 删除/读取消息 （update）
     *
     * @params msgUpdate
     * @return int
     * @access public
     */
    public int updateMsgRecStatus(MsgUpdate msgUpdate) {
        return msgInboxMapper.updateMsgRecStatus(msgUpdate);
    }

    /**
     * 删除/读取消息 （update）
     *
     * @params msgUpdate
     * @return int
     * @access public
     */
    public int updateMsgRecState(MsgUpdate msgUpdate) {
        return msgInboxMapper.updateMsgRecState(msgUpdate);
    }

    /**
     * 删除/读取消息 （update）
     *
     * @params msgUpdate
     * @return int
     * @access public
     */
    public int updateAllMsgRecStatus(MsgUpdate msgUpdate) {
        return msgInboxMapper.updateAllMsgRecStatus(msgUpdate);
    }

    /**
     * 删除/读取消息 （update）
     *
     * @params msgUpdate
     * @return int
     * @access public
     */
    public int updateAllMsgRecState(MsgUpdate msgUpdate) {
        return msgInboxMapper.updateAllMsgRecState(msgUpdate);
    }


}
