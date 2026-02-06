package com.watech.starwars.model.auth;

public record LoginResponse(String accessToken, String refreshToken, String user) {}
