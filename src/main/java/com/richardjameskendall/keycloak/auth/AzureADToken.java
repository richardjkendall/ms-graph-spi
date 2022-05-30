package com.richardjameskendall.keycloak.auth;

import com.google.gson.annotations.SerializedName;

public class AzureADToken {
    private String access_token;
    private int expires_in;
    private int refresh_expires_in;
    private String token_type;
    private String id_token;

    @SerializedName("not-before-policy")
    private int not_before_policy;

    private String scope;

    @SerializedName("accessTokenExpiration")
    private String access_token_expiration;

    private int ext_expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public int getRefresh_expires_in() {
        return refresh_expires_in;
    }

    public void setRefresh_expires_in(int refresh_expires_in) {
        this.refresh_expires_in = refresh_expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }

    public int getNot_before_policy() {
        return not_before_policy;
    }

    public void setNot_before_policy(int not_before_policy) {
        this.not_before_policy = not_before_policy;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAccess_token_expiration() {
        return access_token_expiration;
    }

    public void setAccess_token_expiration(String access_token_expiration) {
        this.access_token_expiration = access_token_expiration;
    }

    public int getExt_expires_in() {
        return ext_expires_in;
    }

    public void setExt_expires_in(int ext_expires_in) {
        this.ext_expires_in = ext_expires_in;
    }
}
