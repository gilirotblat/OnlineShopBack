package gilir.backendfinalpro.service;

import gilir.backendfinalpro.dto.OrderRequestDto;
import gilir.backendfinalpro.dto.OrderResponseDto;

public interface OrderService {
    OrderResponseDto processOrder(OrderRequestDto orderRequest);
}
