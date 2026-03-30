package fr.alban30so.dev.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
public class Fridge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // ex: "Frigo comptoir", "Cave"

    @ManyToOne
    @JoinColumn(name = "bar_id", nullable = false)
    @JsonIgnore
    private Bar bar;

    @OneToMany(mappedBy = "fridge", cascade = CascadeType.ALL)
    private List<StockItem> stockItems;
}