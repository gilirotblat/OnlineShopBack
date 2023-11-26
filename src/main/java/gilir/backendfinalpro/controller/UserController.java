package gilir.backendfinalpro.controller;
import gilir.backendfinalpro.dto.*;
import gilir.backendfinalpro.entity.User;
import gilir.backendfinalpro.service.OrderService;
import gilir.backendfinalpro.service.UserDetailsServiceImpl;
import gilir.backendfinalpro.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
   private final UserService userService;
   private final OrderService orderService;
    private final ModelMapper modelMapper;
    @PutMapping("/cart")
    public ResponseEntity<UserResponseCartDto> updateCart(
            @Valid @RequestBody CartDTO dto) {
        return ResponseEntity.ok(userService.updateCart(getUserName(), dto));
    }

    @PostMapping("/newOrder")
    public ResponseEntity<OrderResponseDto> newOrder(
            @Valid @RequestBody OrderRequestDto dto) {
        return ResponseEntity.ok(orderService.processOrder(dto));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> me() {
        UserResponseDto user = userService.me(getUserName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    public String getUserName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String)auth.getPrincipal();
        return username;
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserWithRoleIds(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String)auth.getPrincipal();
        User user = userService.getUserByUserName(username);

        // Create a UserResponseDto with role IDs
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .cart(modelMapper.map(user.getCurrentCart(), UserResponseCartDto.class))
                .roleIds(userService.getUserById(id).getRoleIds())
                .build();
        return ResponseEntity.ok(userResponseDto);
    }

}
