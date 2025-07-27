package com.vendormanagement.vendor_management_system.dto;

public class AuthResponse {

    private String token;

    // Required for deserialization (e.g., in tests or other APIs)
    public AuthResponse() {
    }

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}