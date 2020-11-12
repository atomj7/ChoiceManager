package com.choicemanager.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;

    private String error;
    private int status;

    {
        this.timestamp = LocalDateTime.now();
    }

    public CustomErrorResponse() {
    }

    public CustomErrorResponse(String error, int status) {
        this.error = error;
        this.status = status;
    }

}
