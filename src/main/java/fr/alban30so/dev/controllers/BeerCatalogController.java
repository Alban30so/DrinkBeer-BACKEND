package fr.alban30so.dev.controllers;

import fr.alban30so.dev.services.BeerCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/catalog")
@CrossOrigin("*")
@RequiredArgsConstructor
public class BeerCatalogController {

    private final BeerCatalogService beerCatalogService;

    // Point d'entrée pour la barre de recherche du front-end
    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> searchBeers(@RequestParam String q) {
        //Prévention des requêtes vide
        if (q == null || q.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<Map<String, Object>> results = beerCatalogService.searchBeers(q);
        return ResponseEntity.ok(results);
    }
}