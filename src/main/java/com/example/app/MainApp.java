package com.example.app;

import java.util.*;

import com.example.data.UserPreferences;
import com.example.services.OntologyService;
import com.example.services.RecommendationService;
import com.example.validators.InputValidator;

public class MainApp {
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

    public int mainHandler(){
        try( Scanner scanner = new Scanner(System.in)){
            System.out.println("Enter the name of the ontology file in .rdf format");
            String filePath = scanner.nextLine();
            OntologyService ontologyService = new OntologyService(filePath);

            RecommendationService recommendationService = new RecommendationService(ontologyService);

            while(true){
                try {
                    System.out.println("Enter information about yourself in the format:\nPreferred genre; solo/multi; preferred platform");
                    String userInput = scanner.nextLine();
                    // Валидация пользовательского ввода
                    if(userInput.equals("exit")){
                        break;
                    }
                    if(userInput.equals("help")){
                        // Вывод всех жанров
                        System.out.println("Genre: " + String.join(", ", KNOWN_GENRES));

                        // Вывод всех платформ
                        System.out.println("Platform: " + String.join(", ", KNOWN_PLATFORM));

                        // Вывод всех режимов
                        System.out.println("Modes: " + String.join(", ", KNOWN_MODES));
                    }
                    InputValidator validator = new InputValidator();
                    ArrayList<ArrayList> data = validator.validate(userInput);
                    recommendationService.provideRecommendation(new UserPreferences(String.join(" ", data.get(0)) ,data.get(1).get(0).toString().equals("multi"), (String) data.get(2).get(0)));
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage() + "\n");
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            return -1;
        }
        return 0;
    }
}
//O:\Itmo\5_SEM\AI system\Lab2\ontology.rdf