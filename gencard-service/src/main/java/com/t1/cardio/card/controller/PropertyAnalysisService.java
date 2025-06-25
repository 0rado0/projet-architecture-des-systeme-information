package com.t1.cardio.card.controller;

import org.springframework.stereotype.Service;
import tp.cpe.ImgToProperties;

import java.util.Map;

@Service
public class PropertyAnalysisService {

    public Map<String, Float> analyzeImageProperties(String imageUrl) {
        String fullUrl = "http://tp.cpe.fr:8088/img-service" + imageUrl;
        return ImgToProperties.getPropertiesFromImg(fullUrl, 100f, 4, 0.3f, false);
    }
}