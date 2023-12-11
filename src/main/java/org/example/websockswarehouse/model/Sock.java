package org.example.websockswarehouse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "socks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 50, nullable = false)
    private String color;
    @Column(name = "cotton_part", nullable = false)
    @Min(0)
    @Max(100)
    private int cottonPart;
    private int quantity;
}
