package com.example.vedioserviceproject.controller;

import com.example.vedioserviceproject.pojo.VideoOverview;
import com.example.vedioserviceproject.pojo.VideoPojo;
import com.example.vedioserviceproject.util.ApiResponseUtil;
import com.example.vedioserviceproject.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/videos")
@CrossOrigin
public class VideoController {

    @Autowired
    private VideoService videoService;

    @CrossOrigin
    @PostMapping("/add")
    public String addVideo(@RequestParam int userId, @RequestParam String title,
                           @RequestParam String token, @RequestParam MultipartFile file) {
        try {
            boolean success = videoService.addVideo(userId, file, title, token);
            if (success) {
                return ApiResponseUtil.success("Video added successfully!", null);
            } else {
                return ApiResponseUtil.failure("Failed to add video.");
            }
        } catch (IllegalArgumentException e) {
            return ApiResponseUtil.failure("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponseUtil.error("An unexpected error occurred: " + e.getMessage());
        }
    }

    @CrossOrigin
    @DeleteMapping("/{videoId}")
    public String deleteVideo(@PathVariable int videoId, @RequestParam String token, @RequestParam int userId) {
        try {
            boolean success = videoService.deleteVideo(videoId, token, userId);
            if (success) {
                return ApiResponseUtil.success("Video deleted successfully!", null);
            } else {
                return ApiResponseUtil.failure("Failed to delete video.");
            }
        } catch (IllegalArgumentException e) {
            return ApiResponseUtil.failure("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponseUtil.error("An unexpected error occurred: " + e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/user/{userId}")
    public String getUserVideos(@PathVariable int userId,
                                @RequestParam String token,
                                @RequestParam(defaultValue = "0") int pageToken,
                                @RequestParam(defaultValue = "5") int pageSize) {
        try {
            Map<String, Object> videos = videoService.getVideoOverviewsByUserId(userId, token, pageToken, pageSize);
            return ApiResponseUtil.success("Videos fetched successfully!", videos);
        } catch (IllegalArgumentException e) {
            return ApiResponseUtil.failure("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponseUtil.error("An unexpected error occurred: " + e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/video/{videoId}")
    public void getVideo(@PathVariable int videoId, HttpServletResponse response) {
        VideoPojo video = videoService.getVideoById(videoId);
        if (video != null) {
            byte[] videoData = video.getVideo(); // Assuming getVideo() returns byte[]
            try (OutputStream os = response.getOutputStream()) {
                response.setContentType("video/mp4"); // 或其他适当的MIME类型
                os.write(videoData);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }

    @CrossOrigin
    @GetMapping("/recommended")
    public String getRecommendedVideos(@RequestParam int userId,
                                       @RequestParam String token,
                                       @RequestParam(defaultValue = "0") int pageToken,
                                       @RequestParam(defaultValue = "5") int pageSize) {
        try {
            Map<String, Object> videos = videoService.getRecommendedVideosWithDetails(userId, token, pageToken, pageSize);
            return ApiResponseUtil.success("Recommended videos fetched successfully!", videos);
        } catch (IllegalArgumentException e) {
            return ApiResponseUtil.failure("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponseUtil.error("An unexpected error occurred: " + e.getMessage());
        }
    }
}
