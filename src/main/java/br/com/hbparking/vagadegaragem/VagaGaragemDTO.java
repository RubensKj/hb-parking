package br.com.hbparking.vagadegaragem;

import br.com.hbparking.cor.Color;
import br.com.hbparking.tipoveiculo.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@NoArgsConstructor
@Data
@AllArgsConstructor
public class VagaGaragemDTO {

    private Long id;
    @Size(max = 20, message = "O Tipo de veiculo não pode conter mais de 20 caracteres.")
    @NotNull(message = "O tipo de veiculo não pode ser nulo.")
    @NotBlank(message = "O tipo de veiculo não pode estar em branco.")
    private VehicleType tipoVeiculo;


    // ~~ parametros opcionais na API ~~
    private Long marca;
    private Long vehicleModel;
    private Color color;
    private String placa;

    @NotNull(message = "O periodo não pode ser nulo.")
    @NotBlank(message = "O periodo não pode estar em branco.")
    private Long periodo;

    @NotNull(message = "O usuario não pode ser nulo.")
    @NotBlank(message = "O usuario não pode estar em branco.")
    private Long usuario;

    private StatusVaga statusVaga;

    public static VagaGaragemDTO of(VagaGaragem vagaGaragem){
        return new VagaGaragemDTO(
                vagaGaragem.getId(),
                vagaGaragem.getTipoVeiculo(),
                vagaGaragem.getMarca().getId(),
                vagaGaragem.getVehicleModel().getId(),
                vagaGaragem.getColor(),
                vagaGaragem.getPlaca(),
                vagaGaragem.getPeriodo().getId(),
                vagaGaragem.getUsuario().getId(),
                vagaGaragem.getStatusVaga()
                );
    }

}
