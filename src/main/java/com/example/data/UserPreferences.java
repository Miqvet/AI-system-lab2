package com.example.data;

import lombok.Data;

/*
POJO class for transport data in system
 */
@Data
public class UserPreferences {
    private final String genre;
    private final boolean mode;
    private final String platform;

    public UserPreferences(String genre, boolean mode, String platform) {
        this.genre = genre;
        this.mode = mode;
        this.platform = platform;
    }
}
