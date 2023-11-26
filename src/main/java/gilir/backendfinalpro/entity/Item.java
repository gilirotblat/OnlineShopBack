package gilir.backendfinalpro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table (uniqueConstraints = {@UniqueConstraint(columnNames = "title")})
public class Item {
    @Id
    @GeneratedValue
    @NotNull
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String description;

    private double price;
    private int stock;

    private String category;
    private String img;

}
