package com.example.vedioserviceproject.services;

import com.example.vedioserviceproject.pojo.DataException;

public interface UserService {
    // boolean isTokenValid(1:required String token, 2:required long useId) throws (1: DataException e),
    boolean isTokenValid(String token, long useId) throws DataException;
}
