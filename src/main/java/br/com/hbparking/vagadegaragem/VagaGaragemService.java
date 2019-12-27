package br.com.hbparking.vagadegaragem;

import br.com.hbparking.marcas.Marca;
import br.com.hbparking.marcas.MarcaService;
import br.com.hbparking.periodo.Periodo;
import br.com.hbparking.periodo.PeriodoService;
import br.com.hbparking.security.user.User;
import br.com.hbparking.security.user.UserService;
import br.com.hbparking.tipoveiculo.VehicleType;
import br.com.hbparking.vehicleModel.VehicleModel;
import br.com.hbparking.vehicleModel.VehicleModelService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class VagaGaragemService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.toString());
    private final IVagaGaragemRepository iVagaGaragemRepository;
    private final MarcaService marcaService;
    private final VehicleModelService vehicleModelService;
    private final PeriodoService periodoService;
    private final ValidadeOnHBEmployee validadeOnHBEmployee;
    private final UserService userService;

    public VagaGaragemDTO save(VagaGaragemDTO vagaGaragemDTO) throws Exception {
        this.validate(vagaGaragemDTO);
        LOGGER.info("Salvando Vaga");
        LOGGER.debug("Vaga: {}", vagaGaragemDTO);
        VagaGaragem vagaSave = this.DtoToVaga(vagaGaragemDTO);
        vagaSave.setStatusVaga(StatusVaga.EMAPROVACAO);
        if (isCarroOrMoto(vagaSave.getTipoVeiculo())) {
            vagaSave.setPlaca(placaValidator(vagaGaragemDTO.getPlaca()));
        }
        ResponseHBEmployeeDTO response = validadeOnHBEmployee.validate("http://localhost:8090/api/teste");
        if (response.getParkingValid()) {
            try {
                vagaSave = this.iVagaGaragemRepository.save(vagaSave);
            } catch (SQLIntegrityConstraintViolationException e) {
                throw new SQLIntegrityConstraintViolationException("A placa informada já está cadastrada no sistema");
            } catch (Exception ex) {
                throw new Exception("Erro ao salvar vaga de garagem {}", ex);
            }
        }


        return VagaGaragemDTO.of(vagaSave);
    }

    public Page<VagaGaragem> findAllByTipoPage(Pageable pageable) {
        LOGGER.info("Retornando vagas em paginas");
        return iVagaGaragemRepository.findAll(pageable);
    }

    public VagaGaragemDTO findById(Long id) {
        Optional<VagaGaragem> vaga = this.iVagaGaragemRepository.findById(id);
        if (vaga.isPresent()) {
            return VagaGaragemDTO.of(vaga.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public VagaGaragemDTO update(VagaGaragemDTO vagaGaragemDTO, Long id) {
        Optional<VagaGaragem> vagaGaragemOptional = this.iVagaGaragemRepository.findById(id);
        if (vagaGaragemOptional.isPresent()) {
            VagaGaragem vagaExsitente = vagaGaragemOptional.get();
            validate(vagaGaragemDTO);
            LOGGER.info("Atualizando vaga... id: [{}]", vagaExsitente.getId());
            LOGGER.debug("Payload: {}", vagaGaragemDTO);
            LOGGER.debug("Vaga Existente: {}", vagaExsitente);
            vagaExsitente = this.DtoToVaga(vagaGaragemDTO);
            return VagaGaragemDTO.of(vagaExsitente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    private void validate(VagaGaragemDTO vagaGaragemDTO) {
        LOGGER.info("Validando Vaga");
        if (vagaGaragemDTO == null) {
            throw new IllegalArgumentException("VagaDTO não deve ser nulo");
        }
        if (vagaGaragemDTO.getPeriodo().equals(null)) {
            throw new IllegalArgumentException("Periodo não deve ser nulo/vazio");
        }
        if (vagaGaragemDTO.getUsuario().equals(null)) {
            throw new IllegalArgumentException("Usuario não deve ser nulo/vazio");
        }
        if (isCarroOrMoto(vagaGaragemDTO.getTipoVeiculo())) {
            if (vagaGaragemDTO.getMarca().equals(null)) {
                throw new IllegalArgumentException("Marca não deve ser nulo/vazio");
            }
            if (vagaGaragemDTO.getVehicleModel().equals(null)) {
                throw new IllegalArgumentException("Modelo não deve ser nulo/vazio");
            }
            if (vagaGaragemDTO.getColor().equals(null)) {
                throw new IllegalArgumentException("Cor não deve ser nulo/vazio");
            }
            if (vagaGaragemDTO.getPlaca().equals(null)) {
                throw new IllegalArgumentException("Placa não deve ser nulo/vazio");
            }
        }
    }

    private VagaGaragem DtoToVaga(VagaGaragemDTO vagaGaragemDTO) {
        Marca marca = marcaService.findById(vagaGaragemDTO.getMarca());
        VehicleModel modelo = vehicleModelService.findById(vagaGaragemDTO.getVehicleModel());
        Periodo periodo = periodoService.findById(vagaGaragemDTO.getPeriodo());
        User usuario = userService.findEntityById(vagaGaragemDTO.getUsuario());
        return new VagaGaragem(
                vagaGaragemDTO.getTipoVeiculo(),
                marca,
                modelo,
                vagaGaragemDTO.getColor(),
                vagaGaragemDTO.getPlaca(),
                periodo,
                usuario,
                vagaGaragemDTO.getStatusVaga()
        );
    }


    public void delete(Long id) {
        LOGGER.info("Executando delete para vaga de ID: [{}]", id);
        this.iVagaGaragemRepository.deleteById(id);
    }

    private String placaValidator(String placa) {
        placa = placa.replaceAll("[^a-zA-Z0-9]", "");
        Pattern pattern = Pattern.compile("[A-Z]{2,3}[0-9]{4}|[A-Z]{3,4}[0-9]{3}|[A-Z0-9]{7}");
        Matcher mat = pattern.matcher(placa);
        if (placa.length() > 0 && placa.length() < 7) {
            if (mat.matches()) {
                return placa.toUpperCase();
            } else {
                throw new IllegalArgumentException("A placa informada não está no formato aceitável");
            }
        } else {
            throw new IllegalArgumentException("A placa informada não está no formato aceitável");
        }
    }

    private boolean isCarroOrMoto(VehicleType vehicleType) {
        if (VehicleType.CARRO == vehicleType || VehicleType.MOTO == vehicleType) {
            return true;
        } else {
            return false;
        }
    }

}
