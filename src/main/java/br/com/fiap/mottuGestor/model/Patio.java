package br.com.fiap.mottuGestor.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_patio;

    @NotBlank(message = "campo obrigatório")
    private String nome;

    @NotBlank(message = "campo obrigatório")
    private String endereco;

    @Positive(message = "deve ser maior que zero")
    private BigDecimal capacidade;
}
