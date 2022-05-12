package com.flyhub.ideaMS.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.flyhub.ideaMS.configurations.GlobalConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 *
 * @author Benjamin E Ndugga
 */
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@ToString
@Data
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder(value = {"timestamp", "totalElements", "operationResult", "operationDescription", "data"})
public class OperationResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = GlobalConfig.APP_TIME_PATTERN)
    private LocalDateTime timestamp = LocalDateTime.now();

    private int totalElements;

    private int operationResult;

    
    private String operationDescription;


    public OperationResponse(int operationResult, String operationDescription) {
        this.operationResult = operationResult;
        this.operationDescription = operationDescription;
    }

    public OperationResponse(int totalElements, int operationResult, String operationDescription) {
        this.totalElements = totalElements;
        this.operationResult = operationResult;
        this.operationDescription = operationDescription;
    }
}
