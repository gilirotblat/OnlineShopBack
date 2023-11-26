package gilir.backendfinalpro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {
    private String orderConfirmationId;
    private long newCartId;
    private String address;
    private String phone;
    private UserResponseCartDto cart;
}
