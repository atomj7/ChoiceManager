package com.choicemanager.domain;

import lombok.Data;

@Data
public class UserRadarChartDTO {

    private User user;
    private RadarChart radarChart;

    public UserRadarChartDTO(User user, RadarChart radarChart) {
        super();
        this.user = user;
        this.radarChart = radarChart;
    }

}
