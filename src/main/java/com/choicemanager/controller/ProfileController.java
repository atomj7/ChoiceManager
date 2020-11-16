package com.choicemanager.controller;

import com.choicemanager.domain.RadarChart;
import com.choicemanager.domain.RadarChartElement;
import com.choicemanager.domain.User;
import com.choicemanager.domain.UserRadarChartDTO;
import com.choicemanager.repository.AnswerRepository;
import com.choicemanager.repository.UserRepository;
import com.choicemanager.utils.ErrorUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AnswerRepository answerRepository;

    public ProfileController(UserRepository userRepository,
                             PasswordEncoder passwordEncoder,
                             AnswerRepository answerRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.answerRepository = answerRepository;
    }


    @GetMapping("/{id}")
    public ResponseEntity profileGet(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            return ResponseEntity.ok(user);
        }
        return new ResponseEntity("user not found",HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity radarChartGet(@PathVariable Long id) {
        RadarChart radarChart = new RadarChart(answerRepository, id);
        if(radarChart.getRadarChart().size() > 0)
            return ResponseEntity.ok(radarChart);
        return new ResponseEntity("Radar chart is empty",HttpStatus.NOT_FOUND);
    }

    @PutMapping
    public ResponseEntity profilePut(@RequestBody @Valid User userData,
                                     BindingResult bindingResult) {
        Map<String, String> errorsMap = ErrorUtils.getErrors(bindingResult);
        if (userData == null) {
            return new ResponseEntity<>("data is null" + errorsMap, HttpStatus.BAD_REQUEST);
        }
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(errorsMap.toString(), HttpStatus.NOT_ACCEPTABLE);
        }
        userData.setPassword(passwordEncoder.encode(userData.getPassword()));
        userRepository.save(userData);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
