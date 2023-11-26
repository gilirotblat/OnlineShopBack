package gilir.backendfinalpro.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var token = getJWTFromRequest(request);

        //if we throw a JWT exception, the chain stops:
        if (StringUtils.hasText(token) && jwtProvider.validateToken(token)){
            var username = jwtProvider.getUsernameFromToken(token);
            var user = userDetailsService.loadUserByUsername(username);

            var authentication = new UsernamePasswordAuthenticationToken(
                    user.getUsername(), null, user.getAuthorities()
            );

            //makes the user logged in:
            SecurityContextHolder.getContext().setAuthentication(authentication);


        }

        //if no exceptions thrown -> the chain continues.
        //proceed with the filter chain:
        filterChain.doFilter(request, response);
    }

    /**
     * get the token from the authorization header:
     * Authorization Header:  "Bearer eyJhbGciOiJIUzIVCJ9.eyJpYXQiOzkwMjJ9.tbDepeoH48wgRQ"
     *
     * @param request with Authorization: Bearer token
     * @return The token from the Authorization Header
     */
    private String getJWTFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authHeader) &&
                StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")) {

            return authHeader.substring("Bearer ".length());
        }

        return null;
    }
}
