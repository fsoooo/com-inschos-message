package com.inschos.message.access.rpc.service;


import com.inschos.message.access.rpc.bean.AccountBean;

/**
 * Created by IceAnt on 2018/4/16.
 */
public interface AccountService {

    AccountBean getAccount(String token);

    AccountBean findByUuid(String uuid);
}
