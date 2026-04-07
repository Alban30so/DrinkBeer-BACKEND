package fr.alban30so.dev.controllers;
import fr.alban30so.dev.models.dto.PublicBarDto;
import fr.alban30so.dev.services.BarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PublicController {

    private final BarService barService; // On injecte le service

    @GetMapping("/bars")
    public ResponseEntity<List<PublicBarDto>> getAllPublicBars() {
        return ResponseEntity.ok(barService.getAllPublicBars());
    }
}