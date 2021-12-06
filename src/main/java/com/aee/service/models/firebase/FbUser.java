package com.aee.service.models.firebase;

import lombok.Data;

import java.util.List;

@Data
public class FbUser {
    private String localId;
    private Boolean emailVerified;
    private List<FbUserDetail> providerUserInfo;

}
