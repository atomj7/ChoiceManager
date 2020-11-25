package com.choicemanager.controller;

import com.choicemanager.domain.RadarChart;
import com.choicemanager.domain.User;
import com.choicemanager.domain.UserDto;
import com.choicemanager.domain.UserRadarChartDTO;
import com.choicemanager.repository.UserRepository;
import com.choicemanager.service.RadarChartService;
import com.choicemanager.service.UserService;
import com.choicemanager.utils.ErrorUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RadarChartService radarChartService;
    private final UserService userService;

    public ProfileController(UserRepository userRepository,
                             PasswordEncoder passwordEncoder,
                             RadarChartService radarChartService, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.radarChartService = radarChartService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> profileGet(@PathVariable Long id) {
        try {
            RadarChart radarChart = new RadarChart(id, radarChartService);
            return ResponseEntity.ok(new UserRadarChartDTO(userService.getUserAsDto(id), radarChart));
        }
        catch(Exception e) {
            return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
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
