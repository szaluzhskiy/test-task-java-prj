package com.example.datstestprj.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDto {

    String code;

    String details;
}
