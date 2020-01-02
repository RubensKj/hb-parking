package br.com.hbparking.vagadegaragem;

import br.com.hbparking.colaborador.Colaborador;
import br.com.hbparking.cor.Color;
import br.com.hbparking.marcas.Marca;
import br.com.hbparking.periodo.Periodo;
import br.com.hbparking.tipoveiculo.VehicleType;
import br.com.hbparking.vehicleModel.VehicleModel;

import javax.persistence.*;


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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_marca", referencedColumnName = "id")
    private Marca marca;

    @ManyToOne(cascade = CascadeType.PERSIST)
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
    @JoinColumn(name = "id_colaborador", referencedColumnName = "id_colaborador", nullable = false)
    private Colaborador colaborador;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusVaga statusVaga;

    public VagaGaragem() {
    }



    public VagaGaragem(VehicleType tipoVeiculo, Marca marca, VehicleModel vehicleModel,
                       Color color, String placa, Periodo periodo, Colaborador colaborador, StatusVaga statusVaga) {
        this.tipoVeiculo = tipoVeiculo;
        this.marca = marca;
        this.vehicleModel = vehicleModel;
        this.color = color;
        this.placa = placa;
        this.periodo = periodo;
        this.colaborador = colaborador;
        this.statusVaga = statusVaga;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VehicleType getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setTipoVeiculo(VehicleType tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public VehicleModel getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(VehicleModel vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    public StatusVaga getStatusVaga() {
        return statusVaga;
    }

    public void setStatusVaga(StatusVaga statusVaga) {
        this.statusVaga = statusVaga;
    }
}
