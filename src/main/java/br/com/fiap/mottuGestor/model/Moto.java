package br.com.fiap.mottuGestor.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Moto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_moto;

    @NotBlank(message = "campo obrigatório")
    @Pattern(regexp = "^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$",message = "Placa inválida. Use o padrão ABC1234 ou ABC1D23")
    private String placa;

    @NotBlank(message = "campo obrigatório")
    @Pattern(regexp = "^[A-Z].*", message = "deve começar com maiúscula")
    private String modelo;

    @NotBlank(message = "campo obrigatório")
    private String rfid_tag;

    @PastOrPresent(message = "não pode ser no futuro")
    @JsonIgnore
    private LocalDate data_cadastro;

    @NotBlank(message = "campo obrigatório")
    private String serviço;

    @ManyToOne
    @JsonIgnore
    private Leitor leitor;
    
}
