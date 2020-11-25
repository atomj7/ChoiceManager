package com.choicemanager.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class RadarChartJsonSerializer extends JsonSerializer<RadarChart> {

    @Override
    public void serialize(RadarChart radarChart, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        if (!radarChart.getRadarChart().isEmpty()) {
            jsonGenerator.writeObjectFieldStart("caption");
            radarChart.getRadarChart().forEach(radarChartElement -> {
                String category = radarChartElement.categoryName;
                try {
                    jsonGenerator.writeStringField(
                            Character.toLowerCase(category.charAt(0)) + category.substring(1),
                            category
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            jsonGenerator.writeEndObject();
            jsonGenerator.writeObjectFieldStart("data");
            radarChart.getRadarChart().forEach(radarChartElement -> {
                String category = radarChartElement.categoryName;
                String averageValue = radarChartElement.averageValue.toString();
                try {
                    jsonGenerator.writeStringField(
                            Character.toLowerCase(category.charAt(0)) + category.substring(1),
                            averageValue
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndObject();
    }

}
