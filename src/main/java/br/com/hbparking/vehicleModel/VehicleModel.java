package br.com.hbparking.vehicleModel;

import br.com.hbparking.marcas.Marca;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "vehicle_models")
@NoArgsConstructor
@Data
public class VehicleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_marca")
    private Marca fkMarca;


    @Column(name = "model", nullable = false)
    private String modelo;

}
