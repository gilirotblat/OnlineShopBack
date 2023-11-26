package gilir.backendfinalpro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDto {
    private Set<CartDTO> cartDTO;
    private long cartId;
    private String address;
    private String phone;
}
