package fr.alban30so.dev.repositories;

import fr.alban30so.dev.models.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BeerRepository extends JpaRepository<Beer, Long> {
    Optional<Beer> findByCatalogBeerId(String catalogBeerId);
    Optional<Beer> findByNameIgnoreCaseAndBrewerIgnoreCase(String name, String brewer);
}