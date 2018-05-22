package com.inschos.message.access.rpc.service;

import com.inschos.message.access.rpc.bean.ChannelBean;

/**
 * Created by IceAnt on 2018/5/21.
 */
public interface ChannelService {
    ChannelBean getChannelDetailById(String id);
}
