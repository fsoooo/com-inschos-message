package com.inschos.message.access.http.controller.action;

import com.inschos.message.access.http.controller.bean.BaseResponse;
import com.inschos.message.access.http.controller.bean.PageBean;
import com.inschos.message.annotation.CheckParamsKit;
import com.inschos.message.assist.kit.JsonKit;
import com.inschos.message.assist.kit.StringKit;
import com.inschos.message.model.Page;

import java.util.ArrayList;
import java.util.List;

public class BaseAction {
    public <T> T requst2Bean(String body, Class<T> clazz) {
        T bean = JsonKit.json2Bean(body, clazz);
        if(bean ==null){
            try {
                bean = clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

    public String json(int code, String message, BaseResponse response) {
        if (response == null) {
            response = new BaseResponse();
        }
        response.code = code;
        CheckParamsKit.Entry<String, String> defaultEntry = CheckParamsKit.getDefaultEntry();
        defaultEntry.details = message;
        List<CheckParamsKit.Entry<String, String>> list = new ArrayList<>();
        list.add(defaultEntry);
        response.message = list;

        return JsonKit.bean2Json(response);
    }

    public String json(int code, List<CheckParamsKit.Entry<String, String>> message, BaseResponse response) {
        if (response == null) {
            response = new BaseResponse();
        }

        response.code = code;
        response.message = message;

        return JsonKit.bean2Json(response);
    }

    public String json(BaseResponse response) {
        return JsonKit.bean2Json(response);
    }

    public List<CheckParamsKit.Entry<String, String>> checkParams(Object obj) {
        List<CheckParamsKit.Entry<String, String>> list = new ArrayList<>();

        if (obj == null) {
            CheckParamsKit.Entry<String, String> anEntry = CheckParamsKit.getDefaultEntry();
            anEntry.details = "解析错误";
            list.add(anEntry);
            return list;
        }

        CheckParamsKit.checkToArray(obj, list);
        if (list.isEmpty()) {
            return null;
        } else {
            return list;
        }
    }

    protected Page setPage(String lastId, String num, String size) {
        Page page = new Page();

        if (StringKit.isInteger(size)) {
            if (StringKit.isInteger(lastId)) {
                page.lastId = Long.valueOf(lastId);
                page.offset = Integer.valueOf(size);
            } else if (StringKit.isInteger(num)) {
                int pageSize = Integer.valueOf(size);
                int pageStart = (Integer.valueOf(num) - 1) * pageSize;

                page.start = pageStart;
                page.offset = pageSize;
            }
        }
        return page;
    }

    protected PageBean setPageBean(long lastId, String page_size, long total, int listSize) {
        PageBean pageBean = new PageBean();
        pageBean.lastId = String.valueOf(lastId);
        pageBean.pageSize = StringKit.isInteger(page_size) ? page_size : "20";
        pageBean.total = String.valueOf(total);
        pageBean.listSize = String.valueOf(listSize);

        return pageBean;
    }

    protected PageBean setPageBean(String page_num, String page_size, long total, int listSize) {
        PageBean pageBean = new PageBean();

        long pageTotal = 0;

        if (StringKit.isInteger(page_size)) {
            int pageSize = Integer.valueOf(page_size);
            if (pageSize > 0) {
                pageTotal = total / pageSize;

                if (total % pageSize > 0) {
                    pageTotal += 1;
                }
            }
        }

        pageBean.pageNum = StringKit.isInteger(page_num) ? page_num : "1";
        pageBean.pageSize = StringKit.isInteger(page_size) ? page_size : "20";
        pageBean.pageTotal = String.valueOf(pageTotal);
        pageBean.total = String.valueOf(total);
        pageBean.listSize = String.valueOf(listSize);

        return pageBean;
    }
}
