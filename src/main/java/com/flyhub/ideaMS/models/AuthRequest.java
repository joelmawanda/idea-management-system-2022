package com.flyhub.ideaMS.models;

import lombok.Data;

@Data
public class AuthRequest {

    private String entityId;
    private String password;
}
