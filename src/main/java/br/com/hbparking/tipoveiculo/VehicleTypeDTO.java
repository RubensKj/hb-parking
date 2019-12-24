package br.com.hbparking.tipoveiculo;

import lombok.*;

@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class VehicleTypeDTO {

    @Getter @Setter
    private int codigo;
    @Getter @Setter
    private String descricao;

}
