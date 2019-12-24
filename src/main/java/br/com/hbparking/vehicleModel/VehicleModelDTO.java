package br.com.hbparking.vehicleModel;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleModelDTO {

    private Long id;
    private Long idMarca;
    private String modelo;

    public static VehicleModelDTO of(VehicleModel vehicleModel){
        return new VehicleModelDTO(
                vehicleModel.getId(),
                vehicleModel.getFkMarca().getId(),
                vehicleModel.getModelo()
        );
    }

}
