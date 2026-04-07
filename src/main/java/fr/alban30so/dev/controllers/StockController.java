package fr.alban30so.dev.controllers;

import fr.alban30so.dev.models.dto.AddStockDto;
import fr.alban30so.dev.models.StockItem;
import fr.alban30so.dev.services.StockService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin("*")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    // DTO interne pour recevoir proprement le JSON du front-end React
    @Data
    public static class AddStockRequest {
        private String catalogBeerId;
        private String format; // "PRESSION", "CANNETTE", "BOUTEILLE"
        private Double price;
        private Double initialVolumeLiters;
    }
    @GetMapping("/fridge/{fridgeId}")
    public ResponseEntity<List<StockItem>> getFridgeInventory(@PathVariable Long fridgeId) {
        List<StockItem> inventory = stockService.getFridgeInventory(fridgeId);
        return ResponseEntity.ok(inventory);
    }

    // Ajouter une bière au frigo
    @PostMapping("/fridge/{fridgeId}")
    public ResponseEntity<StockItem> addBeerToFridge(
            @PathVariable Long fridgeId,
            @RequestBody AddStockDto payload) {

        StockItem savedItem = stockService.addBeerToFridge(fridgeId, payload);
        return ResponseEntity.ok(savedItem);
    }

    @PutMapping("/fridge/{fridgeId}/{stockId}")
    public ResponseEntity<StockItem> updateStockItem(
            @PathVariable Long fridgeId,
            @PathVariable Long stockId,
            @RequestBody AddStockDto payload) {
        return ResponseEntity.ok(stockService.updateStockItem(stockId, payload));
    }

    @DeleteMapping("/fridge/{fridgeId}/{stockId}")
    public ResponseEntity<Void> deleteStockItem(
            @PathVariable Long fridgeId,
            @PathVariable Long stockId) {
        stockService.deleteStockItem(stockId);
        return ResponseEntity.noContent().build();
    }

    // Retirer du volume (ex: "J'ai servi une pinte de 0.5L")
    @PutMapping("/{stockItemId}/consume")
    public ResponseEntity<StockItem> consumeBeer(
            @PathVariable Long stockItemId,
            @RequestParam Double consumedVolumeLiters) {

        StockItem updatedItem = stockService.consumeBeer(stockItemId, consumedVolumeLiters);
        return ResponseEntity.ok(updatedItem);
    }
}