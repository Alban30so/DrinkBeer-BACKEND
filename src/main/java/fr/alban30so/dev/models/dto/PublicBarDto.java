package fr.alban30so.dev.models.dto;

import lombok.Data;
import java.util.List;

@Data
public class PublicBarDto {
    private Long id;
    private String name;
    private List<PublicMenuDto> menu;
}