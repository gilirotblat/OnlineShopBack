package gilir.backendfinalpro.dto;

import gilir.backendfinalpro.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

//User without a password:
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private long id;
    private String username;    
    private String email;
    private UserResponseCartDto cart;
    private Set<Long> roleIds;

}