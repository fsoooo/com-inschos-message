package com.inschos.message.data.mapper;
import com.inschos.message.model.*;
//todo 注意,这个是接口-interface
//Mapper接口开发需要遵循以下规范：
//1、Mapper.xml文件中的namespace与mapper接口的类路径相同。
//2、Mapper接口方法名和Mapper.xml中定义的每个statement的id相同
//3、Mapper接口方法的输入参数类型和mapper.xml中定义的每个sql 的parameterType的类型相同
//4、Mapper接口方法的输出参数类型和mapper.xml中定义的每个sql的resultType的类型相同

public interface MessageMapper {

    int insert(Message Message);//定义返回数据类型-整形-影响数据库行数

    int update(Message update);//定义返回数据类型-整形

    Message findOne(long id);//定义返回数据类型-对象

    int insertName(String name);//定义返回数据类型-整形

}
