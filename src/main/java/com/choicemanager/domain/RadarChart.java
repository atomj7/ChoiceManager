package com.choicemanager.domain;

import com.choicemanager.repository.AnswerRepository;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class RadarChart {

    private List<RadarChartElement> radarChart;

    public RadarChart(AnswerRepository answerRepository, Long id) {
        radarChart = new ArrayList<>();
        HashMap<String, List<Double>> categoryValueMap = new HashMap<>();
        List<Answer> answerList = answerRepository.findAllByUserId(id);

        for (Answer answer : answerList) {
            if (answer.getQuestion().getType().equals("scale")) {
                if (!categoryValueMap.containsKey(answer.getQuestion().getCategory().getName())) {
                    categoryValueMap.put(answer.getQuestion().getCategory().getName(), new ArrayList<>());
                }
                categoryValueMap.get(answer.getQuestion().getCategory().getName()).add(Double.valueOf(answer.getValue()));
            }
        }

        categoryValueMap.forEach((categoryName, list) -> radarChart.add(
                new RadarChartElement(
                        categoryName,
                        list.stream().mapToDouble(Double::doubleValue).sum() / list.size()
                )
        ));
    }
}