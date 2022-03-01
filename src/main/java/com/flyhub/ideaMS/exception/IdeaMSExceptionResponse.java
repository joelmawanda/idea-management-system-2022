package com.flyhub.ideaMS.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.flyhub.ideaMS.models.OperationResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Joel Mawanda
 * @author Jean Kekirunga
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class IdeaMSExceptionResponse extends OperationResponse {

    private String requestParameterName;
    private String requestParameterType;

    private List<Violation> violations;

    public List<Violation> getViolations() {

        if (violations == null) {
            violations = new ArrayList<>();
        }
        return violations;
    }
}
