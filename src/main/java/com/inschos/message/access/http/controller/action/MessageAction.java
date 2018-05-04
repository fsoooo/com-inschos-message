package com.inschos.message.access.http.controller.action;

import com.inschos.message.data.dao.MessageDAO;
import com.inschos.message.kit.JsonKit;
import com.inschos.message.kit.StringKit;
import com.inschos.message.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//业务逻辑层，相当于PHP的logic-接收controller的数据，处理后调用DAO层
@Component
public class MessageAction {
    @Autowired//自动加载
    private MessageDAO messageDAO;
    //String 定义方法返回数据类型
    public String find(String id){
        if(StringKit.isInteger(id)){
            //返回值必须转换成定义成的数据类型
            return "data is :" + JsonKit.bean2Json(messageDAO.findOne(Long.valueOf(id)));
        }
        return "not is int or long";
    }

    public String add(String name){
        if(StringKit.isEmpty(name)){
            return "name is empty";
        }
        return String.valueOf(messageDAO.insert(name));
    }
}
