package br.com.fiap.mottuGestor.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Moviment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_moviment;

    @PastOrPresent(message = "não pode ser no futuro")
    private LocalDate data_evento;

    @ManyToOne
    @JsonIgnore
    private Patio patio;

    @ManyToOne
    @JsonIgnore
    private Leitor leitor;

    @ManyToOne
    @JsonIgnore
    private Moto moto;

    @ManyToOne
    @JsonIgnore
    private User user;

    @NotNull(message = "campo obrigatório")
    @Enumerated(EnumType.STRING)
    private MovimentType movimentType;
    
}
