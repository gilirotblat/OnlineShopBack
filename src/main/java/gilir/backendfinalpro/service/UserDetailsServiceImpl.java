package gilir.backendfinalpro.service;


import gilir.backendfinalpro.dto.SignUpRequestDto;
import gilir.backendfinalpro.dto.UserResponseCartDto;
import gilir.backendfinalpro.dto.UserResponseDto;
import gilir.backendfinalpro.entity.Cart;
import gilir.backendfinalpro.entity.User;
import gilir.backendfinalpro.error.BadRequestException;
import gilir.backendfinalpro.error.ShopException;
import gilir.backendfinalpro.repository.CartRepository;
import gilir.backendfinalpro.repository.RoleRepository;
import gilir.backendfinalpro.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Qualifier("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    //props:
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto signUp(SignUpRequestDto dto) {
        //1) get the user role from role repository:
        val userRole = roleRepository.findByNameIgnoreCase("ROLE_USER")
                .orElseThrow(() -> new ShopException("Please contact admin"));
        //2) if email/username exists -> Go Sign in (Exception)
        val byUser = userRepository.findByUsernameIgnoreCase(dto.getUsername().trim());
        val byEmail = userRepository.findByEmailIgnoreCase(dto.getEmail().trim());

        if (byEmail.isPresent()) {
            throw new BadRequestException("email", "Email already exists");
        } else if (byUser.isPresent()) {
            throw new BadRequestException("username", "Username already exists");
        }

        //3) val user = new User(... encoded-password )
        var user = new User(
                null,
                dto.getUsername(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword().trim()),
              //null,
                Set.of(userRole),
                null,
                new HashSet<>()
        );
        var cart = Cart.builder().items(new HashSet<>()).user(user).build();
        cartRepository.save(cart);
        var carts = new HashSet<Cart>();
        carts.add(cart);
        user.setCarts(carts);
        var savedUser = userRepository.save(user);
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .cart(modelMapper.map(savedUser.getCurrentCart(), UserResponseCartDto.class))
                .roleIds(savedUser.getRoleIds())
                .build();
        return userResponseDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //fetch our user entity from our database
        var user = userRepository
                .findByUsernameIgnoreCase(username).orElseThrow(() -> new UsernameNotFoundException(username));

        var roles = user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName())).toList();

        //return new org.springframework.security.core.userdetails.User
        //spring User implements UserDetails
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), roles);

    }
}
