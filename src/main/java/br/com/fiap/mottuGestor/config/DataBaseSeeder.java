package br.com.fiap.mottuGestor.config;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fiap.mottuGestor.model.Leitor;
import br.com.fiap.mottuGestor.model.Moto;
import br.com.fiap.mottuGestor.model.Moviment;
import br.com.fiap.mottuGestor.model.MovimentType;
import br.com.fiap.mottuGestor.model.Patio;
import br.com.fiap.mottuGestor.model.StatusType;
import br.com.fiap.mottuGestor.model.User;
import br.com.fiap.mottuGestor.model.UserRole;
import br.com.fiap.mottuGestor.repository.LeitorRepository;
import br.com.fiap.mottuGestor.repository.MotoRepository;
import br.com.fiap.mottuGestor.repository.MovimentRepository;
import br.com.fiap.mottuGestor.repository.PatioRepository;
import br.com.fiap.mottuGestor.repository.UserRepository;
import jakarta.annotation.PostConstruct;

@Component
public class DataBaseSeeder {
     @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatioRepository patioRepository;

    @Autowired
    private LeitorRepository leitorRepository;

    @Autowired
    private MotoRepository motoRepository;

    @Autowired
    private MovimentRepository movimentRepository;

    @PostConstruct
    public void seed() {
        // USERS
        var users = List.of(
            User.builder().nome("Felipe").email("felipe@fiap.com").password("12345").role(UserRole.USER).build(),
            User.builder().nome("Samir").email("samir@fiap.com").password("12345").role(UserRole.ADMIN).build()
        );
        userRepository.saveAll(users);

        // PATIOS
        var patios = new ArrayList<Patio>();
        for (int i = 1; i <= 10; i++) {
            patios.add(Patio.builder()
                .nome("Pátio " + i)
                .endereco("Rua Exemplo, " + i)
                .capacidade(300.0 + i * 10)
                .build());
        }
        patioRepository.saveAll(patios);

        // LEITORES
        var leitores = new ArrayList<Leitor>();
        for (int i = 0; i < 10; i++) {
            leitores.add(Leitor.builder()
                .nome("Leitor " + (i+1))
                .status(i % 2 == 0 ? StatusType.ATIVO : StatusType.INATIVO)
                .patio(patios.get(new Random().nextInt(patios.size())))
                .build());
        }
        leitorRepository.saveAll(leitores);

        // MOTOS
        var motos = new ArrayList<Moto>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            String placa = gerarPlacaValida(random);
            motos.add(Moto.builder()
                    .placa(placa)
                    .modelo("Modelo" + i)
                    .rfid_tag("RFID" + i)
                    .data_cadastro(LocalDate.now().minusDays(random.nextInt(100)))
                    .servico("Locação")
                    .leitor(leitores.get(new Random().nextInt(leitores.size())))
                    .build());
        }
        motoRepository.saveAll(motos);

        // MOVIMENTS
        var moviments = new ArrayList<Moviment>();
        for (int i = 0; i < 10; i++) {
            moviments.add(Moviment.builder()
                .data_evento(LocalDate.now().minusDays(random.nextInt(30)))
                .patio(patios.get(new Random().nextInt(patios.size())))
                .leitor(leitores.get(new Random().nextInt(leitores.size())))
                .moto(motos.get(new Random().nextInt(motos.size())))
                .user(users.get(new Random().nextInt(users.size())))
                .movimentType(MovimentType.values()[random.nextInt(MovimentType.values().length)])
                .build());
        }
        movimentRepository.saveAll(moviments);
    }

    private String gerarPlacaValida(Random random) {
        // Gera uma placa no formato ABC1D23 (padrão Mercosul)
        char letra1 = (char) ('A' + random.nextInt(26));
        char letra2 = (char) ('A' + random.nextInt(26));
        char letra3 = (char) ('A' + random.nextInt(26));
        int digito1 = random.nextInt(10);
        char letraOuNumero = random.nextBoolean()
                ? (char) ('A' + random.nextInt(26))
                : (char) ('0' + random.nextInt(10));
        int digito2 = random.nextInt(10);
        int digito3 = random.nextInt(10);
    
        return "" + letra1 + letra2 + letra3 + digito1 + letraOuNumero + digito2 + digito3;
    }
}
