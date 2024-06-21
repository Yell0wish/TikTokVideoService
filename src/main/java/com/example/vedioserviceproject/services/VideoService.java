package com.example.vedioserviceproject.services;

import com.example.vedioserviceproject.pojo.VideoOverview;
import com.example.vedioserviceproject.pojo.VideoPojo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface VideoService {
    public boolean addVideo(int userId, MultipartFile file, String title, String token) throws IllegalArgumentException;

    public boolean deleteVideo(int videoId, String token, int userId) throws IllegalArgumentException;

    Map<String, Object> getVideoOverviewsByUserId(int userId, String token, int pageToken, int pageSize) throws IllegalArgumentException;

    public VideoPojo getVideoById(int videoId);

    public Map<String, Object> getRecommendedVideosWithDetails(int userId, String token, int pageToken, int pageSize) throws IllegalArgumentException;
}
