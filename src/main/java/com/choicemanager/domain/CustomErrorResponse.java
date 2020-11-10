package com.choicemanager.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class CustomErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    @Setter
    @Getter
    private LocalDateTime timestamp;

    @Setter
    @Getter
    private int status;

    @Setter
    @Getter
    private String error;

    public CustomErrorResponse() {
    }

    public CustomErrorResponse(LocalDateTime timestamp, int status, String error) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
    }

}
