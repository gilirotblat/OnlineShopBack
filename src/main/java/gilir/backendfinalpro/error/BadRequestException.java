package gilir.backendfinalpro.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class BadRequestException extends ShopException {
    private final String resourceName;
    public BadRequestException(String resourceName) {
        super("%s was invalid".formatted(resourceName));
        this.resourceName = resourceName;
    }

    public BadRequestException(String resourceName, String message) {
        super(message);
        this.resourceName = resourceName;
    }
}
