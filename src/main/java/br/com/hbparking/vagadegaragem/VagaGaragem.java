package br.com.hbparking.vagadegaragem;

import br.com.hbparking.cor.Color;
import br.com.hbparking.marcas.Marca;
import br.com.hbparking.periodo.Periodo;
import br.com.hbparking.security.user.User;
import br.com.hbparking.tipoveiculo.VehicleType;
import br.com.hbparking.vehicleModel.VehicleModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vaga_garagem")
public class VagaGaragem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_veiculo" , nullable = false)
    private VehicleType tipoVeiculo;

    @ManyToOne
    @JoinColumn(name = "id_marca", referencedColumnName = "id")
    private Marca marca;

    @ManyToOne
    @JoinColumn(name = "id_modelo", referencedColumnName = "id_modelo")
    private VehicleModel vehicleModel;

    @Enumerated(EnumType.STRING)
    @Column(name = "cor")
    private Color color;

    @Column(name = "placa")
    private String placa;

    @ManyToOne
    @JoinColumn(name = "id_periodo", referencedColumnName = "id", nullable = false)
    private Periodo periodo;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = false)
    private User usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusVaga statusVaga;

    public VagaGaragem(VehicleType tipoVeiculo, Marca marca, VehicleModel vehicleModel,
                       Color color, String placa, Periodo periodo, User usuario, StatusVaga statusVaga) {
        this.tipoVeiculo = tipoVeiculo;
        this.marca = marca;
        this.vehicleModel = vehicleModel;
        this.color = color;
        this.placa = placa;
        this.periodo = periodo;
        this.usuario = usuario;
        this.statusVaga = statusVaga;
    }



}
