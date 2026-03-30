package fr.alban30so.dev.repositories;

import fr.alban30so.dev.models.Bar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BarRepository extends JpaRepository<Bar, Long> {

    // Essentiel : Trouver le bar lié à l'utilisateur actuellement connecté
    Optional<Bar> findByOwnerKeycloakId(String ownerKeycloakId);
}