package fr.alban30so.dev.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class StockItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fridge_id", nullable = false)
    @JsonIgnore //Evite la boucle infinie due aux relations
    private Fridge fridge;

    @ManyToOne
    @JoinColumn(name = "beer_id", nullable = false)
    private Beer beer;

    private String format; // "PRESSION", "CANNETTE", "BOUTEILLE"
    private Double price;

    // Quantité disponible en litres
    private Double volumeInLiters;

    //Vérification du stock
    public boolean isOutOfStock() {
        return this.volumeInLiters == null || this.volumeInLiters <= 0;
    }
}