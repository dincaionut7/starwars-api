package com.watech.starwars.model;

public record CharacterResponse(
    String name,
    double height,
    double mass,
    String birthYear,
    int numberOfFilms,
    String dateAdded) {}
