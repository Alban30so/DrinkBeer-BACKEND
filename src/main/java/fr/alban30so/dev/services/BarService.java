package fr.alban30so.dev.services;

import fr.alban30so.dev.models.Bar;
import fr.alban30so.dev.repositories.BarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BarService {

    private final BarRepository barRepository;

    public Bar getBarByOwner(String keycloakId) {
        return barRepository.findByOwnerKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("Aucun bar trouvé pour cet utilisateur"));
    }

    public Bar createBar(String name, String keycloakId) {
        Bar bar = new Bar();
        bar.setName(name);
        bar.setOwnerKeycloakId(keycloakId);
        return barRepository.save(bar);
    }
}