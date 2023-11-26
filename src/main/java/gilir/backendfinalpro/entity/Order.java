package gilir.backendfinalpro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "order_table")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String address;
    private String phone;

    private String confirmationId;
    @OneToOne(optional = true)
    @JoinColumn(name = "cart_id", nullable = true) // Define the foreign key column
    private Cart cart;

}
