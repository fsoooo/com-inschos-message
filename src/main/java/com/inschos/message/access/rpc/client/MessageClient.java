package com.inschos.message.access.rpc.client;

import com.inschos.message.access.rpc.bean.MessageBean;
import com.inschos.message.access.rpc.service.MessageService;
import com.inschos.message.assist.kit.L;
import hprose.client.HproseHttpClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageClient {
    private static final Logger logger = Logger.getLogger(MessageClient.class);
    private final String uri = "/rpc/message";
    private String host = "http://127.0.0.1:8080";

    private MessageService getService() {
        return new HproseHttpClient(host + uri).useService(MessageService.class);
    }

    public MessageBean.Response sendMessage(MessageBean.Request request) {
        logger.info("执行rpc中-service");
        try {
            MessageService service = getService();
            return service != null ? service.sendMessage(request) : null;

        } catch (Exception e) {
            L.log.error("remote fail {}", e.getMessage(), e);
            return null;
        }
    }
}
