package com.watech.starwars.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record Person(
    String name,
    String height,
    String mass,
    @JsonProperty("hair_color") String hairColor,
    @JsonProperty("skin_color") String skinColor,
    @JsonProperty("eye_color") String eyeColor,
    @JsonProperty("birth_year") String birthYear,
    String gender,
    String homeworld,
    List<String> films,
    List<String> species,
    List<String> vehicles,
    List<String> starships,
    String created,
    String edited,
    String url) {}
