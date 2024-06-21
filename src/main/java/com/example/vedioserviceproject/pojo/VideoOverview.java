package com.example.vedioserviceproject.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class VideoOverview {
    private int uuid;
    private int userId;
    private String title;
    private LocalDateTime uploadDate;
    private int likes;
}
