package com.kolade.demo_spring_oauth2.util;

public class Constants {

    public static final Long REFRESH_TOKEN_EXPIRATION = 432000000L; // 5 days in milliseconds
    public static final Long ACCESS_TOKEN_EXPIRATION = 3600000L; // 1 hour in milliseconds
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String JWT_ISSUER = "qlish";
    public static final String AUTHORITIES = "authorities";
    public static final String AUDIENCE = "qlish-api";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
    public static final String ACCESS_DENIED = "You do not have permission to access this page";
    public static final String AUTHENTICATED_MESSAGE = "User authenticated successfully!";
    public static final String REGISTERED_MESSAGE = "User registered successfully!";

    public static final String API_VERSION = "v1";
    public static final String BASE_URL = "/api/" + API_VERSION;
    public static final String LOGOUT_URL = BASE_URL + "/auth/logout";
    public static final String USER_NOT_FOUND = "User does not exist.";

    public static final String[] PUBLIC_URLS = {
            BASE_URL + "/auth/**",
            "/oauth2/**",
            BASE_URL + "/oauth2/**",
            "/h2-console/**"
    };
}
