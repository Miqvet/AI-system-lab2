package com.example.validators;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
    // Заранее известные жанры
    private static final Set<String> KNOWN_GENRES = new HashSet<>(
            Arrays.asList("any", "rpg", "mafia", "fps", "action_rpg", "sandbox", "rogalick", "platformer", "gacha", "battle_royale", "adventure"
    ));

    // Заранее известные типы игр
    private static final Set<String> KNOWN_MODES = new HashSet<>(
            Arrays.asList("solo","multi"
            ));

    // Заранее известные платформы
    private static final Set<String> KNOWN_PLATFORM = new HashSet<>(
            Arrays.asList("any", "pc","mobile", "nintendo", "ps4"
            ));

    private static String REGEX;
    static Pattern pattern;

    static {
        // Создаем строку, содержащую данные, разделенные "|"
        String genres = String.join("|", KNOWN_GENRES);
        String modes = String.join("|", KNOWN_MODES);
        String platforms = String.join("|", KNOWN_PLATFORM);

        // Формируем регулярное выражение с подставленными платформами
        REGEX = String.format("(?i)[_, a-zA-Zа-яА-Я]+;\\s*[a-zA-Zа-яА-Я]+;\\s*[4a-zA-Zа-яА-Я]+");
        pattern = Pattern.compile(REGEX);
    }

    public ArrayList<ArrayList> validate(String input) {
        Matcher matcher = pattern.matcher(input);
        input = input.toLowerCase();
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid input format. Expected 'Genre; game mode; platform'");
        }
        String[] parts = input.split(";");

        // Разбираем жанры
        String[] inputGenres = parts[0].split(",");
        ArrayList<String> validGenres = checkContains(KNOWN_GENRES, inputGenres);
        // Разбираем режимы
        String[] inputModes = parts[1].split(",");
        ArrayList<String> validModes = checkContains(KNOWN_MODES, inputModes);
        // Разбираем платформы
        String[] inputPlatformes = parts[2].split(",");
        ArrayList<String> validPlatform = checkContains(KNOWN_PLATFORM, inputPlatformes);

        // Возвращаем результат в формате List[] (Set жанров и List режимов)
        ArrayList<ArrayList> result = new ArrayList<>();
        result.add(validGenres);  // Преобразуем Set в ArrayList
        result.add(validModes);
        result.add(validPlatform);
//        System.out.println(result);
        return result;
    }

    private static ArrayList<String> checkContains(Set<String> data, String[] values) throws IllegalArgumentException{
        ArrayList<String> validData= new ArrayList<>();
        for (String value : values) {
            value = value.trim();
            if (data.contains(value)) {
                validData.add(value);
            } else {
                throw new IllegalArgumentException("Invalid parameter entered:" + value);
            }
        }
        return validData;
    }
}
