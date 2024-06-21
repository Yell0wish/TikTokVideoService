package com.example.vedioserviceproject.pojo;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class VideoPojo {
    private int uuid;
    private int userId;
    private byte[] video;
    private String title;
    private LocalDateTime uploadDate;
    private int deleted = 0;
    private int likes = 0;
}
