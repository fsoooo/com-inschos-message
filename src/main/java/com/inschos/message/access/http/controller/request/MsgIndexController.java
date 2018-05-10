package com.inschos.message.access.http.controller.request;

import com.inschos.message.access.http.controller.action.MsgIndexAction;
import com.inschos.message.assist.kit.HttpKit;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/message")
public class MsgIndexController {

    @Autowired
    private MsgIndexAction msgIndexAction;
    private static final Logger logger = Logger.getLogger(MsgModelController.class);
    @RequestMapping("/add/**")
    @ResponseBody
    public String addMessage(HttpServletRequest request){
        String body = HttpKit.readRequestBody(request);
        return msgIndexAction.addMessage(body);
    }
}
