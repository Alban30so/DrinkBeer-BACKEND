package fr.alban30so.dev.services;

import fr.alban30so.dev.models.dto.AddStockDto;
import fr.alban30so.dev.models.Fridge;
import fr.alban30so.dev.models.StockItem;
import fr.alban30so.dev.models.Beer;
import fr.alban30so.dev.repositories.BeerRepository;
import fr.alban30so.dev.repositories.FridgeRepository;
import fr.alban30so.dev.repositories.StockItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockItemRepository stockItemRepository;
    private final FridgeRepository fridgeRepository;
    private final BeerRepository beerRepository;

    // Ajouter une nouvelle ligne de stock dans un frigo
    @Transactional
    public StockItem addBeerToFridge(Long fridgeId, AddStockDto dto) {
        Fridge fridge = fridgeRepository.findById(fridgeId)
                .orElseThrow(() -> new RuntimeException("Frigo introuvable"));

        Beer beer = null;

        if (dto.getCatalogBeerId() != null && !dto.getCatalogBeerId().trim().isEmpty()) {
            beer = beerRepository.findByCatalogBeerId(dto.getCatalogBeerId()).orElse(null);
        }

        if (beer == null) {
            beer = beerRepository.findByNameIgnoreCaseAndBrewerIgnoreCase(dto.getName(), dto.getBrewer()).orElse(null);
        }

        if (beer == null) {
            beer = new Beer();
            if (dto.getCatalogBeerId() == null || dto.getCatalogBeerId().trim().isEmpty()) {
                beer.setCatalogBeerId("MANUAL-" + java.util.UUID.randomUUID().toString());
            } else {
                beer.setCatalogBeerId(dto.getCatalogBeerId());
            }

            beer.setName(dto.getName());
            beer.setBrewer(dto.getBrewer());
            if (dto.getStyle() != null && !dto.getStyle().trim().isEmpty()) {
                beer.setStyle(dto.getStyle());
            } else {
                beer.setStyle("Non spécifié");
            }
            beer.setAbv(0.0);

            beer = beerRepository.save(beer);
        }

        StockItem stockItem = new StockItem();
        stockItem.setFridge(fridge);
        stockItem.setBeer(beer);
        stockItem.setFormat(dto.getFormat());
        stockItem.setPrice(dto.getPrice());
        stockItem.setVolumeInLiters(dto.getInitialVolumeLiters());


        return stockItemRepository.save(stockItem);
    }

    // Mettre à jour un stock existant (Prix, Format, Volume)
    @Transactional
    public StockItem updateStockItem(Long stockItemId, AddStockDto dto) {
        StockItem stockItem = stockItemRepository.findById(stockItemId)
                .orElseThrow(() -> new RuntimeException("Ligne de stock introuvable"));

        stockItem.setFormat(dto.getFormat());
        stockItem.setPrice(dto.getPrice());
        stockItem.setVolumeInLiters(dto.getInitialVolumeLiters());

        return stockItemRepository.save(stockItem);
    }

    // Supprimer une ligne de stock
    @Transactional
    public void deleteStockItem(Long stockItemId) {
        if (!stockItemRepository.existsById(stockItemId)) {
            throw new RuntimeException("Ligne de stock introuvable");
        }
        stockItemRepository.deleteById(stockItemId);
    }

    // Gestion du service des bière
    @Transactional
    public StockItem consumeBeer(Long stockItemId, Double consumedVolumeLiters) {
        StockItem stockItem = stockItemRepository.findById(stockItemId)
                .orElseThrow(() -> new RuntimeException("Stock introuvable"));

        // 1. Vérifier si on a assez de stock pour servir
        if (stockItem.getVolumeInLiters() < consumedVolumeLiters) {
            throw new RuntimeException("Pas assez de volume en stock !");
        }

        // 2. Calculer le nouveau volume
        double newVolume = stockItem.getVolumeInLiters() - consumedVolumeLiters;

        // 3. Règle métier : S'il reste moins de 25cl, on considère le contenant comme vide.
        if (newVolume < 0.25) {
            newVolume = 0.0;
        }

        stockItem.setVolumeInLiters(newVolume);

        return stockItemRepository.save(stockItem);
    }

    // Récupérer l'inventaire complet d'un frigo
    public List<StockItem> getFridgeInventory(Long fridgeId) {
        return stockItemRepository.findByFridgeId(fridgeId);
    }
}