package fr.alban30so.dev.repositories;

import fr.alban30so.dev.models.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockItemRepository extends JpaRepository<StockItem, Long> {

    // Récupérer tout le contenu d'un frigo
    List<StockItem> findByFridgeId(Long fridgeId);

    // Récupérer uniquement les bières en stock (volume > 0) pour l'affichage client
    List<StockItem> findByFridgeIdAndVolumeInLitersGreaterThan(Long fridgeId, Double volume);
}