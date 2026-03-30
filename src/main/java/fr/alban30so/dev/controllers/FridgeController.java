package fr.alban30so.dev.controllers;

import fr.alban30so.dev.models.Fridge;
import fr.alban30so.dev.services.FridgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fridges")
@CrossOrigin("*")
@RequiredArgsConstructor
public class FridgeController {

    private final FridgeService fridgeService;

    // Créer un nouveau frigo pour un bar
    @PostMapping("/bar/{barId}")
    public ResponseEntity<Fridge> createFridge(@PathVariable Long barId, @RequestParam String name) {
        Fridge fridge = fridgeService.createFridge(barId, name);
        return ResponseEntity.ok(fridge);
    }

    // Lister tous les frigos d'un bar (utile pour le tableau de bord du proprio)
    @GetMapping("/bar/{barId}")
    public ResponseEntity<List<Fridge>> getFridgesByBar(@PathVariable Long barId) {
        List<Fridge> fridges = fridgeService.getFridgesByBar(barId);
        return ResponseEntity.ok(fridges);
    }
}