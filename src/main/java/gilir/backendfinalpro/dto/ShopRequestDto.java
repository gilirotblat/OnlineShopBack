package gilir.backendfinalpro.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopRequestDto {
    @NotNull
    @Size(min = 2, max = 255)
   // @UniqueTitle
    private String title;

    @NotNull
    @Size(min = 2, max = 1000)
    private String description;

    private String img;

    @Min(value = 0)
    private double price;


    @Min(value = 0)
    private int stock;

    private String category;

}