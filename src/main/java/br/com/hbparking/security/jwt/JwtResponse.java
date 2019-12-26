package br.com.hbparking.security.jwt;

public class JwtResponse {

    private String type = "Bearer";
    private String token;

    public JwtResponse(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
