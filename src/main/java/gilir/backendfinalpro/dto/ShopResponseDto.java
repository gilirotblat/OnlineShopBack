package gilir.backendfinalpro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopResponseDto {
    private Long id;
    private String title;
    private String description;

    private double price;

    private String category;
    private int stock;
    private String img;
}