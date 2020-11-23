package com.choicemanager.service;

import com.choicemanager.domain.Answer;
import com.choicemanager.domain.RadarChartElement;
import com.choicemanager.repository.AnswerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("RadarChartService")
public class RadarChartService {

    private final AnswerRepository answerRepository;

    RadarChartService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public List<RadarChartElement> getRadarChart(Long id) {
        List<RadarChartElement> radarChart = new ArrayList<>();
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

        categoryValueMap.forEach((categoryName, list) -> {
            if (!list.isEmpty()) {
                Double value = list.stream().mapToDouble(Double::doubleValue).sum() / list.size();
                radarChart.add(
                        new RadarChartElement(
                                categoryName,
                                value
                        )
                );
            }
        });

        return radarChart;
    }

}
