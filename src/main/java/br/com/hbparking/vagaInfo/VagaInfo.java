package br.com.hbparking.vagaInfo;

import br.com.hbparking.periodo.Periodo;
import br.com.hbparking.tipoveiculo.VehicleType;

import javax.persistence.*;

@Entity(name = "vaga_info")
public class VagaInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantidade", nullable = false)
    private int quantidade;

    @JoinColumn(name = "id_periodo", referencedColumnName = "id")
    @OneToOne
    private Periodo periodo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_veiculo")
    private VehicleType vehicleType;

    private double valor;

    public VagaInfo() {
    }

    public VagaInfo(Long id, int quantidade, double valor, VehicleType vehicleType, Periodo periodo) {
        this.id = id;
        this.quantidade = quantidade;
        this.valor = valor;
        this.vehicleType = vehicleType;
        this.periodo = periodo;
    }

    public VagaInfo(int quantidade, double valor, VehicleType vehicleType, Periodo periodo) {
        this.quantidade = quantidade;
        this.valor = valor;
        this.vehicleType = vehicleType;
        this.periodo = periodo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
