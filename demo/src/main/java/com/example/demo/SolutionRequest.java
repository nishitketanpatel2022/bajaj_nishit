package com.bajaj.BajajTask.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SolutionRequest {
    private String finalQuery;

}
