package br.com.hbparking.csv;

import lombok.*;

@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VehicleGroupDTO {

    @Getter @Setter
    private Long codigoMarca;
    @Getter @Setter
    private String modelo;

}
