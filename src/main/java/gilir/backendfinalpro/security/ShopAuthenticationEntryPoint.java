package gilir.backendfinalpro.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

/**
 * Used by the <code>ExceptionTranslationFilter</code> to return a status and a message
 * Copied from: {@link BasicAuthenticationFilter}.
 * <p>
 */
public class ShopAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException {

        response.sendError(HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized. Please refer to Api docs to get an API Key");
    }
}
