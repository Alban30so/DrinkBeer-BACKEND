package fr.alban30so.dev.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Beer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String catalogBeerId; // ID catalog.beer API

    private String name;
    private String brewer;//Brasseur/Fabriquant
    private String style;//Type de bière
    private Double abv; // Degré d'alcool
}