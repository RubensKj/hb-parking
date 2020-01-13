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
    private Long colaborador;

    private StatusVaga statusVaga;

    public static VagaGaragemDTO of(VagaGaragem vagaGaragem) {
        VagaGaragemDTO vagaGaragemDTO = new VagaGaragemDTO();
        vagaGaragemDTO.setId(vagaGaragem.getId());
        vagaGaragemDTO.setPeriodo(vagaGaragem.getPeriodo().getId());
        vagaGaragemDTO.setColaborador(vagaGaragem.getColaborador().getId());
        vagaGaragemDTO.setStatusVaga(vagaGaragem.getStatusVaga());
        vagaGaragemDTO.setTipoVeiculo(vagaGaragem.getTipoVeiculo());

        if (vagaGaragem.getTipoVeiculo() == VehicleType.CARRO ||
                vagaGaragem.getTipoVeiculo() == VehicleType.MOTO) {
            vagaGaragemDTO.setMarca(vagaGaragem.getMarca().getId());
            vagaGaragemDTO.setVehicleModel(vagaGaragem.getVehicleModel().getId());
            vagaGaragemDTO.setColor(vagaGaragem.getColor());
            vagaGaragemDTO.setPlaca(vagaGaragem.getPlaca());
        } else {
            vagaGaragemDTO.setMarca(null);
            vagaGaragemDTO.setVehicleModel(null);
            vagaGaragemDTO.setColor(null);
            vagaGaragemDTO.setPlaca(null);
        }
        return vagaGaragemDTO;
    }

    public VagaGaragemDTO(VehicleType tipoVeiculo, Long marca, Long vehicleModel, Color color,
                          String placa, Long periodo, Long colaborador, StatusVaga statusVaga) {
        this.tipoVeiculo = tipoVeiculo;
        this.marca = marca;
        this.vehicleModel = vehicleModel;
        this.color = color;
        this.placa = placa;
        this.periodo = periodo;
        this.colaborador = colaborador;
        this.statusVaga = statusVaga;
    }
}
