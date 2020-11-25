package com.choicemanager.controller;

import com.choicemanager.domain.RadarChart;
import com.choicemanager.domain.UserDto;
import com.choicemanager.repository.AnswerRepository;
import com.choicemanager.repository.UserRepository;
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
    private final AnswerRepository answerRepository;
    private final UserService userService;

    public ProfileController(UserRepository userRepository,
                             PasswordEncoder passwordEncoder,
                             AnswerRepository answerRepository, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.answerRepository = answerRepository;
        this.userService = userService;
    }


    @GetMapping("/{id}")
    public ResponseEntity profileGet(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUserAsDto(id));
        }
        catch(Exception e){
            return new ResponseEntity("user not found",HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/radar/{id}")
    public ResponseEntity radarChartGet(@PathVariable Long id) {
        RadarChart radarChart = new RadarChart(answerRepository, id);
        if(radarChart.getRadarChart().size() > 0)
            return ResponseEntity.ok(radarChart);
        return new ResponseEntity("Radar chart is empty",HttpStatus.NOT_FOUND);
    }

    @PutMapping
    public ResponseEntity profilePut(@RequestBody @Valid UserDto userDto,
                                     BindingResult bindingResult) {
        Map<String, String> errorsMap = ErrorUtils.getErrors(bindingResult);
        if (userDto == null) {
            return new ResponseEntity<>("data is null" + errorsMap, HttpStatus.BAD_REQUEST);
        }
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(errorsMap.toString(), HttpStatus.NOT_ACCEPTABLE);
        }
        if(userService.saveUser(userDto)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>("save error", HttpStatus.I_AM_A_TEAPOT);
    }
}
