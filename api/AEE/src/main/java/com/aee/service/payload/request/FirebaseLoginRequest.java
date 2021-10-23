package com.aee.service.payload.request;

import com.aee.service.form.CheckAccountForm;
import com.aee.service.form.CreateAccountForm;
import com.aee.service.models.firebase.FbUser;
import lombok.Data;

import java.util.List;

@Data
public class FirebaseLoginRequest {
    private List<FbUser> users;

    public boolean validationAccountData(CreateAccountForm createAccountForm){
        try {
            FbUser fbUser = users.get(0);
            return fbUser.getLocalId().equals(createAccountForm.getFirebaseUserId());
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean validationAccountData(CheckAccountForm checkAccountForm){
        try {
            FbUser fbUser = users.get(0);
            return fbUser.getLocalId().equals(checkAccountForm.getFirebaseUserId());
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public String getProviderId(){
        try {
            return users.get(0).getProviderUserInfo().get(0).getProviderId();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String getFirebaseId(){
        try {
            return users.get(0).getLocalId();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String getUrlPhoto(){
        try {
            return users.get(0).getProviderUserInfo().get(0).getPhotoUrl();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
