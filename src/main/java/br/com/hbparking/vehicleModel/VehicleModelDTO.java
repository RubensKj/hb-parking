package br.com.hbparking.vehicleModel;

import lombok.*;

@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VehicleModelDTO {

    @Getter @Setter
    private Long id;
    @Getter @Setter
    private Long idMarca;
    @Getter @Setter
    private String modelo;

    public static VehicleModelDTO of(VehicleModel vehicleModel){
        return new VehicleModelDTO(
                vehicleModel.getId(),
                vehicleModel.getFkMarca().getId(),
                vehicleModel.getModelo()
        );
    }

}
