package gilir.backendfinalpro.error;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ToString
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends ShopException {
    private String resourceName; //Post
    private String resourceId;//id = 7
    private String message;//not found

    //Designated constructor:
    public ResourceNotFoundException(String resourceName, String resourceId, String message) {
        super("%s with id = %s %s".formatted(resourceName, resourceId, message));
        this.resourceName = resourceName;
        this.resourceId = resourceId;
        this.message = message;
    }

    //convenience constructor:
    public ResourceNotFoundException(String resourceName, long resourceId, String message) {
        //use the other constructor:
        this(resourceName, String.valueOf(resourceId), message);
    }

    //convenience constructor:
    public ResourceNotFoundException(String resourceName, long resourceId) {
        //use the other constructor:
        this(resourceName, String.valueOf(resourceId), "Not Found");
    }

    //convenience constructor:
    public ResourceNotFoundException(long resourceId) {
        //use the other constructor:
        this("Resource", String.valueOf(resourceId), "Not Found");
    }
}
//%d = מספרים שלמים בבסיס עשרוני decimal integer
//%x = מספרים שלמים בבסיס 16 hex integer
//%o = מספרים שלמים בבסיס 8 octal integer
//%f = float מספרים עשרוני
//%s = String