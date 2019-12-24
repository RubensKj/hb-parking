package br.com.hbparking.vehicleModel;

import br.com.hbparking.marcas.Marca;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "vehicle_models")
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class VehicleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo")
    @Getter @Setter
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_marca")
    @Getter @Setter
    private Marca fkMarca;


    @Column(name = "model", nullable = false)
    @Getter @Setter
    private String modelo;

}
