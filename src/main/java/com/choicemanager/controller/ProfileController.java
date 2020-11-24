package com.choicemanager.controller;

import com.choicemanager.domain.RadarChart;
import com.choicemanager.domain.User;
import com.choicemanager.domain.UserRadarChartDTO;
import com.choicemanager.repository.UserRepository;
import com.choicemanager.service.RadarChartService;
import com.choicemanager.utils.ErrorUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RadarChartService radarChartService;

    public ProfileController(UserRepository userRepository,
                             PasswordEncoder passwordEncoder,
                             RadarChartService radarChartService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.radarChartService = radarChartService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> profileGet(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            RadarChart radarChart = new RadarChart(id, radarChartService);
            return ResponseEntity.ok(new UserRadarChartDTO(user, radarChart));
        }
        return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
    }

    @PutMapping
    public ResponseEntity<?> profilePut(@RequestBody @Valid User userData,
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
