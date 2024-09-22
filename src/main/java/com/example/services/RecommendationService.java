package com.example.services;

import com.example.data.UserPreferences;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;

public class RecommendationService {
    private final OntologyService queryService;

    public RecommendationService(OntologyService queryService) {
        this.queryService = queryService;
    }

    public void provideRecommendation(UserPreferences preferences) {
        String genre = preferences.getGenre();
        String platform = preferences.getPlatform();
        boolean mode = preferences.isMode();
//        System.out.println(genre + "\t" + platform + "\t" + mode);

        // Стартовая часть запроса
        StringBuilder sparqlQuery = new StringBuilder(
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                        "PREFIX ex: <http://www.example.org/ontologies/videogames#>\n" +
                        "SELECT ?videoGame ?online ?releaseYear\n" +
                        "WHERE {\n" +
                        "  ?videoGame ex:has_Platform ?platform.\n" +
                        "  ?videoGame ex:has_Genre ?genre.\n" +
                        "  ?videoGame ex:release_Year ?releaseYear.\n"
        );

        sparqlQuery.append("?videoGame a ex:").append(mode ? "MultiplayerGame.\n" : "SinglePlayerGame.\n");

        // Добавляем фильтрацию по платформе, если platform != "Любые"
        if (!"any".equals(platform)) {
//            System.out.println(platform + "\t" + genre);
            sparqlQuery.append("  ?videoGame ex:has_Platform ex:").append(platform).append(" .\n");
        }
        // Добавляем фильтрацию по жанру, если genre != "Любые"
        if (!"any".equals(genre)) {
            String[] genres = genre.split(" ");
            sparqlQuery.append("  ?videoGame ex:has_Genre ?genre .\n");
            if (genres.length == 1) {
                // Если только один жанр, просто добавляем его
                sparqlQuery.append("  FILTER(?genre = ex:").append(genres[0]).append(")\n");
            } else {
                // Если несколько жанров, добавляем их через логическое OR
                sparqlQuery.append("  FILTER (");
                for (int i = 0; i < genres.length; i++) {
                    sparqlQuery.append("?genre = ex:").append(genres[i]);
                    if (i < genres.length - 1) {
                        sparqlQuery.append(" || ");
                    }
                }
                sparqlQuery.append(")\n");
            }
//            sparqlQuery.append("  ?videoGame ex:has_Genre ex:").append(genre).append(" .\n");
        }


        // Добавляем обязательные части запроса
        sparqlQuery.append(
                "  ?videoGame ex:online ?online.\n" +
                        "}\n" +
                        "ORDER BY DESC(?online)"
        );

        // Вывод финального SPARQL-запроса
//        System.out.println(sparqlQuery.toString());

        // Выполнение запроса к онтологии
        ResultSet results = queryService.queryOntology(sparqlQuery.toString());
        if (results.hasNext()) {
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();

                // Получаем название игры из URI
                RDFNode gameNode = solution.get("videoGame");
                String gameName = gameNode.isResource() ? gameNode.asResource().getLocalName() : gameNode.toString();

                // Получаем значение свойства online как число
                RDFNode onlineNode = solution.get("online");
                String onlineStatus = "N/A";
                if (onlineNode != null && onlineNode.isLiteral()) {
                    onlineStatus = String.valueOf(onlineNode.asLiteral().getString()); // Извлекаем как целое число
                }

                // Получаем значение свойства release_Year как число
                RDFNode releaseYearNode = solution.get("releaseYear");
                String releaseYearStatus = "N/A";
                if (releaseYearNode != null && releaseYearNode.isLiteral()) {
                    releaseYearStatus = String.valueOf(releaseYearNode.asLiteral().getString()); // Извлекаем как целое число
                }

                // Выводим результат
                System.out.println("Game: " + gameName + ", Online per day: " + onlineStatus + ", Release Year: " + releaseYearStatus);
            }

            System.out.println();
        } else {
            // Если записей нет, выводим сообщение
            System.out.println("No such games\n");
        }
    }
}
