package com.example.vedioserviceproject.dao;

import com.example.vedioserviceproject.pojo.VideoOverview;
import com.example.vedioserviceproject.pojo.VideoPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface VideoDAO {
    boolean addVideo(VideoPojo video);

    @Update("UPDATE videos SET deleted = 1 WHERE uuid = #{videoId}")
    boolean markVideoAsDeleted(@Param("videoId") int videoId);

    @Select("SELECT COUNT(*) > 0 FROM videos WHERE uuid = #{videoId} AND userid = #{userId} AND deleted = 0")
    boolean isVideoOwnedByUser(@Param("videoId") int videoId, @Param("userId") int userId);

    @Select("SELECT COUNT(*) FROM videos WHERE userId = #{userId} AND deleted = 0")
    int getTotalCount(@Param("userId") int userId);

    @Select("SELECT uuid, userId, title, uploadDate, likes FROM videos WHERE userId = #{userId} AND deleted = 0 LIMIT #{pageSize} OFFSET #{offset}")
    List<VideoOverview> getVideoOverviewsByUserId(@Param("userId") int userId, @Param("offset") int offset, @Param("pageSize") int pageSize);

    @Select("SELECT * FROM videos WHERE uuid = #{videoId} AND deleted = 0")
    VideoPojo getVideoById(@Param("videoId") int videoId);

    @Select("SELECT * FROM videos WHERE deleted = 0 ORDER BY likes DESC LIMIT #{pageSize} OFFSET #{offset}")
    List<VideoPojo> getRecommendedVideosWithDetails(@Param("offset") int offset, @Param("pageSize") int pageSize);

    @Select("SELECT COUNT(*) FROM videos WHERE deleted = 0")
    int getTotalCountOfVideos();
}
