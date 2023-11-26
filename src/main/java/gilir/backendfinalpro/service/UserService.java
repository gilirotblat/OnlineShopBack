package gilir.backendfinalpro.service;


import gilir.backendfinalpro.dto.CartDTO;
import gilir.backendfinalpro.dto.UserResponseCartDto;
import gilir.backendfinalpro.dto.UserResponseDto;
import gilir.backendfinalpro.entity.User;

import java.util.Optional;

public interface UserService {

    UserResponseCartDto updateCart(String username, CartDTO cartDTO);

    UserResponseDto me(String username);
    User getUserById (Long id);
    User getUserByUserName (String userName);

   User saveUser(User user);
}
