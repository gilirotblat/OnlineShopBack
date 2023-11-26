package gilir.backendfinalpro.service;

import gilir.backendfinalpro.dto.CartDTO;

import gilir.backendfinalpro.dto.UserResponseCartDto;
import gilir.backendfinalpro.dto.UserResponseDto;
import gilir.backendfinalpro.entity.Cart;
import gilir.backendfinalpro.entity.CartItem;
import gilir.backendfinalpro.entity.Item;
import gilir.backendfinalpro.entity.User;
import gilir.backendfinalpro.error.ShopException;
import gilir.backendfinalpro.repository.CartItemRepository;
import gilir.backendfinalpro.repository.CartRepository;
import gilir.backendfinalpro.repository.ShopRepository;
import gilir.backendfinalpro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    private final ShopRepository shopRepository;

    private final CartItemRepository cartItemRepository;

    private final ModelMapper modelMapper;

    @Override
    public UserResponseCartDto updateCart(String username, CartDTO cartDTO) {
        User user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new ShopException("No user with username " + username));
        Cart cart = user.getCurrentCart();
        CartItem cartItem = null;
        for(var item : cart.getItems()) {
            if(item.getItem().getId() == cartDTO.getItemId()) {
                cartItem = item;
                break;
            }
        }
        if (cartItem == null && cartDTO.getQuantity() > 0) { // first time adding this item to cart
            Item item = shopRepository.findById(cartDTO.getItemId()).orElseThrow(() -> new ShopException("No item with id" + cartDTO.getItemId()));
            cart.getItems().add(CartItem.builder().item(item).cart(cart).quantity(cartDTO.getQuantity()).build());
        } else if(cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + cartDTO.getQuantity());
            if(cartItem.getQuantity() <= 0) {
                cart.getItems().remove(cartItem);
                cartItemRepository.delete(cartItem);
            }
        }
           cartRepository.save(cart);
           user.getCarts().add(cart);
           userRepository.save(user);
           return modelMapper.map(cart, UserResponseCartDto.class);
    }

    @Override
    public UserResponseDto me(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new ShopException("No user found with username " + username));
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .cart(modelMapper.map(user.getCurrentCart(), UserResponseCartDto.class))
                .roleIds(user.getRoleIds())
                .build();
        return userResponseDto;
    }
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ShopException("No user found with id " + id));
    }

    @Override
    public User getUserByUserName(String userName) {
        return userRepository.findByUsernameIgnoreCase(userName)
                .orElseThrow(() -> new ShopException("No user found with userName " + userName));
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
