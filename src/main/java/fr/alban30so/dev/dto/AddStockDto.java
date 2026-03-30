package fr.alban30so.dev.dto;

import lombok.Data;

@Data
public class AddStockDto {
    private String catalogBeerId;
    private String name;
    private String brewer;
    private String style;
    private String format;
    private Double price;
    private Double initialVolumeLiters;
}