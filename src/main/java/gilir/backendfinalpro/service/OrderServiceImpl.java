package gilir.backendfinalpro.service;

import gilir.backendfinalpro.dto.OrderRequestDto;
import gilir.backendfinalpro.dto.OrderResponseDto;
import gilir.backendfinalpro.dto.UserResponseCartDto;
import gilir.backendfinalpro.entity.Cart;
import gilir.backendfinalpro.entity.Order;
import gilir.backendfinalpro.entity.User;
import gilir.backendfinalpro.error.ResourceNotFoundException;
import gilir.backendfinalpro.repository.CartRepository;
import gilir.backendfinalpro.repository.OrderRepository;
import gilir.backendfinalpro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final CartRepository cartRepository;

    private final OrderRepository orderRepository;

    public OrderResponseDto processOrder(OrderRequestDto orderRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String)auth.getPrincipal();
        User user = userService.getUserByUserName(username);
        // set new cart
        Cart cart = user.getCurrentCart();

        // create a order and attach the cart
        String confirmationId = UUID.randomUUID().toString();
        Order order = Order.builder()
                .address(orderRequest.getAddress())
                .phone(orderRequest.getPhone())
                .cart(cart)
                .confirmationId(confirmationId)
                .build();
        order = orderRepository.save(order);
        cart.setOrder(order);
        // add the order to the user
        user.getOrders().add(order);
        var newCart = Cart.builder().items(new HashSet<>()).user(user).build();
        cartRepository.save(newCart);
        user.getCarts().add( newCart);
        User savedUser = userService.saveUser(user);
        UserResponseCartDto orderCart = modelMapper.map(cart, UserResponseCartDto.class);
        return new OrderResponseDto(confirmationId,
                savedUser.getCurrentCart().getId(),
                orderRequest.getAddress(),
                orderRequest.getPhone(),
                orderCart);
    }
}
