package fr.alban30so.dev.models.dto;

import lombok.Data;

@Data
public class PublicMenuDto {
    private String beerName;
    private String brewer;
    private String style;
    private String format; // PRESSION, BOUTEILLE, CANNETTE
    private boolean outOfStock;
}