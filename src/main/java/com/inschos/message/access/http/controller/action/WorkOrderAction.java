package com.inschos.message.access.http.controller.action;

import com.inschos.message.access.http.controller.bean.BaseResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;

@Component
public class WorkOrderAction {
    private static final Logger logger = Logger.getLogger(MsgModelAction.class);
    @Autowired

    /**
     * 创建工单
     *
     * @param name        工单标题
     * @param classify     类型
     * @param content      内容
     * @return json
     * @access public
     */

    public String addWork(HttpServletRequest request) {
        BaseResponse response = new BaseResponse();

        if (request == null){
//            return json(BaseResponse.CODE_FAILURE,"params is empty",response);
        }

            return "not is int or long";

    }

}
