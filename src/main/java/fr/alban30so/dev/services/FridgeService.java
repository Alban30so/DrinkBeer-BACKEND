package fr.alban30so.dev.services;

import fr.alban30so.dev.models.Bar;
import fr.alban30so.dev.models.Fridge;
import fr.alban30so.dev.repositories.BarRepository;
import fr.alban30so.dev.repositories.FridgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FridgeService {

    private final FridgeRepository fridgeRepository;
    private final BarRepository barRepository;

    public List<Fridge> getFridgesByBar(Long barId) {
        return fridgeRepository.findByBarId(barId);
    }

    public Fridge createFridge(Long barId, String name) {
        Bar bar = barRepository.findById(barId)
                .orElseThrow(() -> new RuntimeException("Bar introuvable"));

        Fridge fridge = new Fridge();
        fridge.setName(name);
        fridge.setBar(bar);
        return fridgeRepository.save(fridge);
    }
    public void deleteFridge(Long fridgeId) {
        fridgeRepository.deleteById(fridgeId);
    }
}