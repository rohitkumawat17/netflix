package com.netflix.security;

public class SecurityConstants {

    public static final String SECRET_KEY = "SecretKeyToGenJWTS";
    public static final long EXPIRATION_TIME = 10 * 24 * 3600 * 1000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
