package com.example.vedioserviceproject.services.impl;

import com.example.vedioserviceproject.dao.VideoDAO;
import com.example.vedioserviceproject.pojo.VideoOverview;
import com.example.vedioserviceproject.pojo.VideoPojo;
import com.example.vedioserviceproject.pojo.VideoPojoBase64;
import com.example.vedioserviceproject.services.UserService;
import com.example.vedioserviceproject.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoDAO videoDAO;

    @Autowired
    private UserService userService;

    @Override
    public boolean addVideo(int userId, MultipartFile file, String title, String token) throws IllegalArgumentException {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be greater than zero");
        }

        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token must not be null or empty");
        }

        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title must not be null or empty");
        }

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Video file must not be null or empty");
        }

        // 验证文件格式是否为 MP4
        if (!"video/mp4".equals(file.getContentType())) {
            throw new IllegalArgumentException("Only MP4 format is accepted");
        }

        try {
            if (!userService.isTokenValid(token, userId)) {
                throw new IllegalArgumentException("Token is invalid");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Token is invalid");
        }

        try {
            byte[] video = file.getBytes();
            VideoPojo videoPojo = new VideoPojo();
            videoPojo.setUserId(userId);
            videoPojo.setVideo(video);
            videoPojo.setTitle(title);
            videoPojo.setUploadDate(LocalDateTime.now());
            videoPojo.setDeleted(0);

            return videoDAO.addVideo(videoPojo);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to process video file");
        }
    }

    @Override
    public boolean deleteVideo(int videoId, String token, int userId) throws IllegalArgumentException {
        if (videoId <= 0) {
            throw new IllegalArgumentException("Video ID must be greater than zero");
        }

        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token must not be null or empty");
        }

        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be greater than zero");
        }

//        try {
//            if (!userService.isTokenValid(token, userId)) {
//                throw new IllegalArgumentException("Token is invalid");
//            }
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Token is invalid");
//        }

        if (!videoDAO.isVideoOwnedByUser(videoId, userId)) {
            throw new IllegalArgumentException("Video does not belong to the user or does not exist");
        }

        return videoDAO.markVideoAsDeleted(videoId);
    }


    @Override
    public Map<String, Object> getVideoOverviewsByUserId(int userId, String token, int pageToken, int pageSize) throws IllegalArgumentException {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be greater than zero");
        }

        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token must not be null or empty");
        }

        if (pageToken < 0) {
            throw new IllegalArgumentException("Page token must be greater than or equal to zero");
        }

        if (pageSize <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero");
        }

//        try {
//            if (!userService.isTokenValid(token, userId)) {
//                throw new IllegalArgumentException("Token is invalid");
//            }
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Token is invalid");
//        }

        int totalCount = videoDAO.getTotalCount(userId);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        if (pageToken >= totalPages) {
            throw new IllegalArgumentException("Page token exceeds total pages");
        }

        int offset = pageToken * pageSize;
        List<VideoOverview> videos = videoDAO.getVideoOverviewsByUserId(userId, offset, Math.min(pageSize, totalCount - offset));

        Map<String, Object> response = new HashMap<>();
        response.put("totalCount", totalCount);
        response.put("pageToken", pageToken);
        response.put("pageSize", pageSize);
        response.put("videos", videos);

        if (offset + pageSize < totalCount) {
            response.put("nextPageToken", pageToken + 1);
        } else {
            response.put("nextPageToken", -1);
        }

        return response;
    }

    public VideoPojo getVideoById(int videoId) {
        return videoDAO.getVideoById(videoId);
    }

    public Map<String, Object> getRecommendedVideosWithDetails(int userId, String token, int pageToken, int pageSize) throws IllegalArgumentException {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be greater than zero");
        }

        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token must not be null or empty");
        }

        if (pageToken < 0) {
            throw new IllegalArgumentException("Page token must be greater than or equal to zero");
        }

        if (pageSize <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero");
        }

//        try {
//            if (!userService.isTokenValid(token, userId)) {
//                throw new IllegalArgumentException("Token is invalid");
//            }
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Token is invalid");
//        }

        int totalCount = videoDAO.getTotalCountOfVideos();
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        if (pageToken >= totalPages) {
            throw new IllegalArgumentException("Page token exceeds total pages");
        }

        int offset = pageToken * pageSize;
        List<VideoPojo> videos = videoDAO.getRecommendedVideosWithDetails(offset, Math.min(pageSize, totalCount - offset));
        List<VideoPojoBase64> videosBase64 = videos.stream().map(video -> {
            VideoPojoBase64 videoBase64 = new VideoPojoBase64();
            videoBase64.setUuid(video.getUuid());
            videoBase64.setUserId(video.getUserId());
            videoBase64.setVideo(Base64.getEncoder().encodeToString(video.getVideo()));
            videoBase64.setTitle(video.getTitle());
            videoBase64.setUploadDate(video.getUploadDate());
            videoBase64.setDeleted(video.getDeleted());
            videoBase64.setLikes(video.getLikes());
            return videoBase64;
        }).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("totalCount", totalCount);
        response.put("pageToken", pageToken);
        response.put("pageSize", pageSize);
        response.put("videos", videosBase64);

        if (offset + pageSize < totalCount) {
            response.put("nextPageToken", pageToken + 1);
        } else {
            response.put("nextPageToken", -1);
        }

        return response;
    }
}
