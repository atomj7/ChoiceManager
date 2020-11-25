package com.choicemanager.controller;

import com.choicemanager.domain.Goal;
import com.choicemanager.domain.User;
import com.choicemanager.repository.UserRepository;
import com.choicemanager.security.CurrentUser;
import com.choicemanager.service.GoalService;
import com.choicemanager.utils.ErrorUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
public class GoalController {
    private final GoalService goalService;
    private final UserRepository userRepository;

    public GoalController(GoalService goalService, UserRepository userRepository) {
        this.goalService = goalService;
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/goals/create")
    public @ResponseBody
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<Object> addGoal(@CurrentUser @RequestBody @Valid Goal goal, User userPrincipal, BindingResult bindingResult) {
        Map<String, String> errorsMap = ErrorUtils.getErrors(bindingResult);
        if (goal == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("data is null" + errorsMap);
        }

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(errorsMap);
        }
        if (!goalService.AddGoal(goal, userRepository.findById(userPrincipal.getId()))) {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(
                    Map.of("message", "goal already exist"));
        }
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(
                Map.of("message", "goal created"));
    }

    @GetMapping("/goals")
    public @ResponseBody
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<Object> goals(User userPrincipal) {
        Optional<User> userOptional = userRepository.findById(userPrincipal.getId());
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(goalService.GetGoals(userOptional));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("user not found");
    }
}

