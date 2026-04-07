package fr.alban30so.dev.services;

import fr.alban30so.dev.models.Beer;
import fr.alban30so.dev.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BeerCatalogService {

    private final BeerRepository beerRepository;
    private final RestTemplate restTemplate;

    @Transactional
    public Beer getOrFetchBeerFromCatalog(String catalogBeerId) {
        return beerRepository.findByCatalogBeerId(catalogBeerId)
                .orElseGet(() -> fetchAndSaveBeerFromExternalApi(catalogBeerId));
    }

    private Beer fetchAndSaveBeerFromExternalApi(String catalogBeerId) {
        String apiUrl = "https://api.catalog.beer/beer/" + catalogBeerId;

        try {
            Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);

            if (response == null) {
                throw new RuntimeException("Réponse vide de catalog.beer");
            }

            Beer newBeer = new Beer();
            newBeer.setCatalogBeerId(catalogBeerId);

            newBeer.setName((String) response.get("name"));

            // Prévention type de ABV
            Object abvObj = response.get("abv");
            if (abvObj != null) {
                newBeer.setAbv(Double.valueOf(abvObj.toString()));
            } else {
                newBeer.setAbv(0.0);
            }

            // Gestion du Brasseur (brewer)
            // Si c'est un objet (LinkedHashMap), on extrait le nom. Si c'est un String, on le prend directement.
            Object brewerObj = response.get("brewer");
            if (brewerObj instanceof Map) {
                Map<String, Object> brewerMap = (Map<String, Object>) brewerObj;
                newBeer.setBrewer((String) brewerMap.get("name"));
            } else if (brewerObj instanceof String) {
                newBeer.setBrewer((String) brewerObj);
            } else {
                newBeer.setBrewer("Brasseur inconnu");
            }

            // Gestion du Style (style) - Même logique
            Object styleObj = response.get("style");
            if (styleObj instanceof Map) {
                Map<String, Object> styleMap = (Map<String, Object>) styleObj;
                newBeer.setStyle((String) styleMap.get("name"));
            } else if (styleObj instanceof String) {
                newBeer.setStyle((String) styleObj);
            } else {
                newBeer.setStyle("Style inconnu");
            }

            return beerRepository.save(newBeer);

        } catch (Exception e) {
            System.err.println("Erreur détaillée lors de la récupération de la bière : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Impossible de récupérer ou sauvegarder la bière depuis l'API externe", e);
        }
    }

    //Recherche de bière sur l'API catalog.beer
    public List<Map<String, Object>> searchBeers(String searchTerm) {
        String apiUrl = "https://api.catalog.beer/beer/search?q=" + searchTerm;

        try {
            Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);

            if (response != null && response.containsKey("data")) {
                List<Map<String, Object>> rawData = (List<Map<String, Object>>) response.get("data");
                List<Map<String, Object>> formattedData = new ArrayList<>();
                //Formatage des données
                for (Map<String, Object> rawBeer : rawData) {
                    Map<String, Object> formattedBeer = new HashMap<>();

                    formattedBeer.put("id", rawBeer.get("id"));
                    formattedBeer.put("name", rawBeer.get("name"));
                    formattedBeer.put("abv", rawBeer.get("abv"));

                    // Gestion sécurisée du brasseur imbriqué
                    Object brewerObj = rawBeer.get("brewer");
                    if (brewerObj instanceof Map) {
                        Map<String, Object> brewerMap = (Map<String, Object>) brewerObj;
                        formattedBeer.put("brewer", brewerMap.get("name"));
                    } else {
                        formattedBeer.put("brewer", "Brasseur inconnu");
                    }

                    // Gestion sécurisée du style
                    Object styleObj = rawBeer.get("style");
                    if (styleObj instanceof String) {
                        formattedBeer.put("style", styleObj);
                    } else if (styleObj instanceof Map) {
                        Map<String, Object> styleMap = (Map<String, Object>) styleObj;
                        formattedBeer.put("style", styleMap.get("name"));
                    } else {
                        formattedBeer.put("style", "Style inconnu");
                    }

                    formattedData.add(formattedBeer);
                }

                return formattedData;
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche sur catalog.beer : " + e.getMessage());
        }

        return List.of();
    }
}