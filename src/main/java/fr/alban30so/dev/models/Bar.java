package fr.alban30so.dev.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Bar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // L'ID provenant de Keycloak pour faire le lien avec l'utilisateur authentifié
    @Column(unique = true, nullable = false)
    private String ownerKeycloakId;

    @OneToMany(mappedBy = "bar", cascade = CascadeType.ALL)
    private List<Fridge> fridges;
}