package gilir.backendfinalpro.controller;


import gilir.backendfinalpro.dto.SignInRequestDto;
import gilir.backendfinalpro.dto.SignInResponseDto;
import gilir.backendfinalpro.dto.SignUpRequestDto;
import gilir.backendfinalpro.dto.UserResponseDto;
import gilir.backendfinalpro.security.JWTProvider;
import gilir.backendfinalpro.service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserDetailsServiceImpl userDetailsService;
    private final JWTProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signUp(@RequestBody @Valid SignUpRequestDto dto) {
        return new ResponseEntity<>(userDetailsService.signUp(dto), HttpStatus.CREATED);
    }


    @PostMapping("/signin")
    public ResponseEntity<SignInResponseDto> signIn(@RequestBody @Valid SignInRequestDto dto) {
        var user = userDetailsService.loadUserByUsername(dto.getUsername());

        var savedPassword = user.getPassword();
        var givenPassword = dto.getPassword();

        if (passwordEncoder.matches(givenPassword, savedPassword)) {
            //grant:
            var token = jwtProvider.generateToken(user.getUsername());

            return ResponseEntity.ok(new SignInResponseDto(token));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
