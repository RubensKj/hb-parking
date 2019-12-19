package br.com.hbparking.vehicleModel;

import br.com.hbparking.marcas.Marca;

import javax.persistence.*;

@Entity
@Table(name = "vehicle_models")
public class VehicleModel {

    public VehicleModel() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_marca")
    private Marca fkMarca;


    @Column(name = "model", nullable = false)
    private String modelo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Marca getFkMarca() {
        return fkMarca;
    }

    public void setFkMarca(Marca fkMarca) {
        this.fkMarca = fkMarca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
}
