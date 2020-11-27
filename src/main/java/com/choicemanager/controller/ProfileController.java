package com.choicemanager.controller;

import com.choicemanager.domain.*;
import com.choicemanager.exception.ResourceNotFoundException;
import com.choicemanager.security.CurrentUser;
import com.choicemanager.service.RadarChartService;
import com.choicemanager.service.UserService;
import com.choicemanager.utils.ErrorUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final RadarChartService radarChartService;
    private final UserService userService;

    public ProfileController(RadarChartService radarChartService, UserService userService) {
        this.radarChartService = radarChartService;
        this.userService = userService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        try {
            User user = userService.getCurrentUser(userPrincipal);
            Long id = user.getId();
            RadarChart radarChart = new RadarChart(id, radarChartService);
            return ResponseEntity.ok(new UserRadarChartDTO(userService.getUserAsDto(id), radarChart));
        }catch(UsernameNotFoundException e) {
            return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
        }catch(ResourceNotFoundException e) {
            return new ResponseEntity<>("current user error", HttpStatus.NOT_FOUND);
        }catch(Exception e) {
            return new ResponseEntity<>("error", HttpStatus.I_AM_A_TEAPOT);
        }
    }


    @PutMapping("/put")
    public ResponseEntity<?> profilePut(@RequestBody @Valid UserDto userDto,
                                        BindingResult bindingResult) {
        Map<String, String> errorsMap = ErrorUtils.getErrors(bindingResult);
        if (userDto == null) {
            return new ResponseEntity<>("data is null" + errorsMap, HttpStatus.BAD_REQUEST);
        }
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(errorsMap.toString(), HttpStatus.NOT_ACCEPTABLE);
        }
        if(userService.saveUser(userDto)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>("save error", HttpStatus.I_AM_A_TEAPOT);

    }
}
