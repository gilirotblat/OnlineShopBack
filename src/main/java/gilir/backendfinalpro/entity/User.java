package gilir.backendfinalpro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String username;
    @NotNull
    private String email;
    @NotNull
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "id"
            )
    )
    private Set<Role> roles;

    @OneToMany
    @JoinTable(
            name = "user_carts",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "cart_id",
                    referencedColumnName = "id"
            )
    )
    private Set<Cart> carts;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Order> orders = new HashSet<>();

    public Set<Long> getRoleIds() {
        return roles.stream()
                .map(Role::getId)
                .collect(Collectors.toSet());
    }

    public Cart getCurrentCart() {
        for(var cart : carts) {
            if(cart.getOrder()==null)
                return cart;
        }
        return null;
    }
}
