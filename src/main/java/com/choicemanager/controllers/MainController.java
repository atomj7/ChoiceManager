package com.choicemanager.controllers;

import com.choicemanager.domain.Goal;
import com.choicemanager.repository.GoalRepository;
import com.choicemanager.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController

public class MainController {
    @Autowired
    private GoalService goalService;
    @Autowired
    private GoalRepository goalRepository;

    @PostMapping(value = "/view")
    public @ResponseBody
    ResponseEntity<Object> home(@RequestBody @Valid Goal goal, BindingResult bindingResult) {
        Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
        if(goal == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("data is null" + errorsMap);
        }

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(errorsMap);
        }
        if (!goalService.AddGoal(goal)) {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(
                    Map.of("message", "goal already exist"));
        }
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(
                Map.of("message", "goal created"));
    }


}

