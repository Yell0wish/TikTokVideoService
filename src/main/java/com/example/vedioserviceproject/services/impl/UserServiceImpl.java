package com.example.vedioserviceproject.services.impl;

import com.example.vedioserviceproject.client.TTSocket;
import com.example.vedioserviceproject.client.ThriftClientConnectPoolFactory;
import com.example.vedioserviceproject.pojo.DataException;
import com.example.vedioserviceproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    ThriftClientConnectPoolFactory thriftPool;


    @Override
    public boolean isTokenValid(String token, long useId) throws DataException {
        TTSocket ttSocket = null;
        boolean result = false;
        try {
            //通过对象池获得一个客户端链接
            ttSocket = thriftPool.getConnect();
            result = ttSocket.getService().isTokenValid(token, useId);
        } catch (Exception e) {
            System.out.println("isTokenValid error" + e.getMessage());
            //出现异常从连接器移出
            thriftPool.invalidateObject(ttSocket);
            ttSocket = null;
        } finally {
            if (ttSocket != null) {
                //归还链接
                thriftPool.returnConnection(ttSocket);
            }
        }
        return result;
    }
}
