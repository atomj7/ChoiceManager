package com.choicemanager.domain;

import lombok.Data;

@Data
public class RadarChartElement {

    String categoryName;
    Double averageValue;

    public RadarChartElement(String categoryName, Double averageValue) {
        this.categoryName = categoryName;
        this.averageValue = averageValue;
    }

}
