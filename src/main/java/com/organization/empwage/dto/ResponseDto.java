package com.organization.empwage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto <T>{
    private boolean isSuccess;
    private Integer code;
    private T data;
    private List<ValidatorDto> errors;

    public ResponseDto(boolean isSuccess, Integer code, T data) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.data = data;
    }
}
