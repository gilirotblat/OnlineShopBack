package gilir.backendfinalpro.dto;

import gilir.backendfinalpro.entity.CartItem;
import gilir.backendfinalpro.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseCartDto {

    public String id;
    public Set<CartItem> items;
}
