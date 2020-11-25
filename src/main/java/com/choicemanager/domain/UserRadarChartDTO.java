package com.choicemanager.domain;

import lombok.Data;

@Data
public class UserRadarChartDTO {

    private UserDto userDto;
    private RadarChart radarChart;

    public UserRadarChartDTO(UserDto userDto, RadarChart radarChart) {
        super();
        this.userDto = userDto;
        this.radarChart = radarChart;
    }

}
