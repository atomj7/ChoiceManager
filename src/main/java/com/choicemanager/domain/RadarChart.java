package com.choicemanager.domain;

import com.choicemanager.service.RadarChartService;
import lombok.Getter;

import java.util.List;

public class RadarChart {

    @Getter
    private final List<RadarChartElement> radarChart;

    public RadarChart(Long id, RadarChartService radarChartService) {
        radarChart = radarChartService.getRadarChart(id);
    }

}