package com.choicemanager.controller;

import com.choicemanager.domain.Goal;
import com.choicemanager.domain.User;
import com.choicemanager.domain.UserPrincipal;
import com.choicemanager.repository.UserRepository;
import com.choicemanager.security.CurrentUser;
import com.choicemanager.service.GoalService;
import com.choicemanager.service.UserService;
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
    private  final UserService userService;

    public GoalController(GoalService goalService, UserService userService) {
        this.goalService = goalService;
        this.userService = userService;
    }

    @PostMapping(value = "/goals/create")
    public @ResponseBody
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<Object> addGoal(@CurrentUser UserPrincipal userPrincipal, @RequestBody @Valid Goal goal, BindingResult bindingResult) {
        Map<String, String> errorsMap = ErrorUtils.getErrors(bindingResult);
        User user = userService.getCurrentUser(userPrincipal);
        if (goal == null || user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("data is null" + errorsMap);
        }

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(errorsMap);
        }
        if (!goalService.AddGoal(goal, user)) {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(
                    Map.of("message", "goal already exist"));
        }
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(
                Map.of("message", "goal created"));
    }

    @GetMapping("/goals")
    public @ResponseBody
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<?> goals(@CurrentUser UserPrincipal userPrincipal) {
        User userOptional = userService.getCurrentUser(userPrincipal);
        if (userOptional != null) {
            return ResponseEntity.ok(goalService.GetGoals(userOptional));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("user not found");
    }

    @PutMapping("/goals")
    public @ResponseBody
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<?> editGoal(@CurrentUser UserPrincipal userPrincipal, @RequestBody @Valid Goal goal, BindingResult bindingResult) {
        User userOptional = userService.getCurrentUser(userPrincipal);
        if(userOptional != null) {
            return ResponseEntity.ok(goalService.EditGoal(goal, userOptional));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("user not found");
    }

    @DeleteMapping("/goals")
    public @ResponseBody
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<?> deleteGoal(@CurrentUser UserPrincipal userPrincipal, @RequestBody @Valid Goal goal) {
        User userOptional = userService.getCurrentUser(userPrincipal);
        if(userOptional != null) {
            return ResponseEntity.ok(goalService.DeleteGoal(goal.getId()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("user not found");
    }

}

