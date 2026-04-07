package fr.alban30so.dev.services;

import fr.alban30so.dev.models.Bar;
import fr.alban30so.dev.models.Fridge;
import fr.alban30so.dev.models.StockItem;
import fr.alban30so.dev.models.dto.PublicBarDto;
import fr.alban30so.dev.models.dto.PublicMenuDto;
import fr.alban30so.dev.repositories.BarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BarService {

    private final BarRepository barRepository;

    public Bar getBarByOwner(String keycloakId) {
        return barRepository.findByOwnerKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("Aucun bar trouvé pour cet utilisateur"));
    }

    public Bar createBar(String name, String address, String keycloakId) {
        Bar bar = new Bar();
        bar.setName(name);
        bar.setAddress(address); // <-- On enregistre l'adresse
        bar.setOwnerKeycloakId(keycloakId);
        return barRepository.save(bar);
    }
    /**
     * NOUVELLE MÉTHODE : Récupère tous les bars et les transforme en DTOs publics.
     */
    public List<PublicBarDto> getAllPublicBars() {
        List<Bar> bars = barRepository.findAll();

        // On transforme chaque entité Bar en PublicBarDto
        return bars.stream()
                .map(this::convertToPublicDto)
                .collect(Collectors.toList());
    }

    /**
     * MÉTHODE PRIVÉE : S'occupe de la logique de transformation (mapping)
     */
    private PublicBarDto convertToPublicDto(Bar bar) {
        PublicBarDto dto = new PublicBarDto();
        dto.setId(bar.getId());
        dto.setName(bar.getName());
        dto.setAddress(bar.getAddress());

        List<PublicMenuDto> menu = new ArrayList<>();

        if (bar.getFridges() != null) {
            for (Fridge fridge : bar.getFridges()) {
                if (fridge.getStockItems() != null) {
                    for (StockItem item : fridge.getStockItems()) {
                        PublicMenuDto menuItem = new PublicMenuDto();
                        menuItem.setBeerName(item.getBeer().getName());
                        menuItem.setBrewer(item.getBeer().getBrewer());
                        menuItem.setStyle(item.getBeer().getStyle());
                        menuItem.setFormat(item.getFormat());
                        menuItem.setOutOfStock(item.getVolumeInLiters() == null || item.getVolumeInLiters() <= 0);

                        menu.add(menuItem);
                    }
                }
            }
        }

        dto.setMenu(menu);
        return dto;
    }
}