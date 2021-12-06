package com.aee.service.models.firebase;

import lombok.Data;

@Data
public class FbUserDetail {
    private String providerId;
    private String displayName;
    private String photoUrl;
    private String email;
}

