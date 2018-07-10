package com.inschos.message.access.rpc.service;

import com.inschos.message.access.rpc.bean.MessageBean;

public interface MessageService {
    /**
     * 发送消息
     * @param request
     * @return
     */
    MessageBean.Response sendMessage(MessageBean.Request request);
}
