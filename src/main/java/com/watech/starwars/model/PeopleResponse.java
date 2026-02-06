package com.watech.starwars.model;

import java.util.List;

public record PeopleResponse(int count, String next, String previous, List<Person> results) {}
