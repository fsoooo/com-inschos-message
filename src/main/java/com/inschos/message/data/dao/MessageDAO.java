package com.inschos.message.data.dao;

import com.inschos.message.data.mapper.MessageMapper;
import com.inschos.message.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
//DAO层
@Component
public class MessageDAO {
    @Autowired
    private MessageMapper messageMapper;
    //Message-定义返回数据类型-对象
    public Message findOne(long id) {
        return messageMapper.findOne(id);
    }
    //int-定义返回数据类型-整形
    public int insert(String name){
        return messageMapper.insertName(name);
    }

}
