package com.inschos.message.access.http.controller.request;

import com.inschos.message.access.http.controller.action.MessageAction;
import com.inschos.message.kit.HttpKit;
import com.inschos.message.kit.StringKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
// 控制器标签-用来接收参数
// 控制器提供访问应用程序的行为，通常通过服务接口定义或注解定义两种方法实现.
// 控制器解析用户的请求并将其转换为一个模型。在Spring MVC中一个控制器可以包含多个Action（动作、方法）。
@Controller
//自定义路由
// value 属性指定映射路径或URL模板
//定义请求方式
//@RequestMapping(value = "/message",method = RequestMethod.POST)
//@RequestMapping(value = "/message",method = RequestMethod.GET)
@RequestMapping(value = "/message")
public class MessageController {

    @Autowired
    private MessageAction messageAction;
    @RequestMapping("/find/**")
    @ResponseBody
    public String find(HttpServletRequest request){
        String body = HttpKit.readRequestBody(request);
        String id = StringKit.splitLast(request.getRequestURI(),"/");
        return messageAction.find(id);
    }
    @RequestMapping("/add/**")
    @ResponseBody
    public String add(HttpServletRequest request){
        String body = HttpKit.readRequestBody(request);
        String name = StringKit.splitLast(request.getRequestURI(),"/");
        return messageAction.add(name);
    }
}
