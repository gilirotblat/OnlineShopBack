package gilir.backendfinalpro.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Shop",
                description = "Online store management",
                version = "1.0",
                contact = @Contact(
                        name = "GiliRot",
                        url = "https://springdoc.org/v2/",
                        email = "gilirotblat@gmail.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                ),
                termsOfService = "https://app.termsfeed.com/wizard/terms-conditions?step=2&data=eyJhZ3JlZW1lbnRfZm9yIjpbIldlYnNpdGUiXX0%3D#step-2"
        )
)

@SecuritySchemes(
        @SecurityScheme(
                name = "Bearer Authentication",
                type = SecuritySchemeType.HTTP,
                bearerFormat = "JWT",
                scheme = "Bearer"
        )
)
public class OpenApiConfig {
}
