package fr.alban30so.dev.repositories;

import fr.alban30so.dev.models.Fridge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FridgeRepository extends JpaRepository<Fridge, Long> {

    // Récupérer la liste des frigos pour un bar donné
    List<Fridge> findByBarId(Long barId);
}