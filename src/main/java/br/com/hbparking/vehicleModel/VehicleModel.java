package br.com.hbparking.vehicleModel;

import javax.persistence.*;

@Entity
@Table(name = "vehicle_models")
public class VehicleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo")
    private Long id;

    @Column(name = "id_marca", nullable = false)
    private Long idMarca;


    @Column(name = "model", nullable = false)
    private String modelo;

    public VehicleModel() {
    }

    public VehicleModel(Long idMarca, String modelo) {
        this.idMarca = idMarca;
        this.modelo = modelo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(Long idMarca) {
        this.idMarca = idMarca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
}
