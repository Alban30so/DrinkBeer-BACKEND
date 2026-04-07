package fr.alban30so.dev.controllers;

import fr.alban30so.dev.models.Bar;
import fr.alban30so.dev.models.dto.PublicBarDto;
import fr.alban30so.dev.services.BarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/bars")
@CrossOrigin("*")
@RequiredArgsConstructor
public class BarController {

    private final BarService barService;

    // Créer un bar
    @PostMapping
    public ResponseEntity<Bar> createBar(
            @RequestParam String name,
            @RequestParam String address,
            Principal principal) {
        String keycloakId = (principal != null) ? principal.getName() : "fake-keycloak-id-pour-tester";//Debug sans keycloak
        Bar newBar = barService.createBar(name, address, keycloakId);
        return ResponseEntity.ok(newBar);
    }

    // Récupère le bar du user
    @GetMapping("/my-bar")
    public ResponseEntity<Bar> getMyBar(Principal principal) {
        String keycloakId = (principal != null) ? principal.getName() : "fake-keycloak-id-pour-tester";
        Bar myBar = barService.getBarByOwner(keycloakId);
        return ResponseEntity.ok(myBar);
    }


}