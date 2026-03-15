package com.tus.RRS.dto.auth;

public class JwtResponse {
	private final String token;
	private String role;

    public JwtResponse(String token,String role) {
        this.token = token;
        this.role = role;
    }

    public String getToken() {
        return token;
    }
    
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
