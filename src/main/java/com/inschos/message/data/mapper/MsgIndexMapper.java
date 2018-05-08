package com.inschos.message.data.mapper;

import com.inschos.message.model.*;

public interface MsgIndexMapper {
    //发送站内信
    int addMsgSys(MsgSys msgSys);//定义返回数据类型-整形-影响数据库行数
    //收取站内信（系统把站内信同步到用户收件箱,同时修改系统发件表的状态）
    int insertMsgRec(MsgRec msgRec);//定义返回数据类型-整形-影响数据库行数

}
