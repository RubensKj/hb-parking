package br.com.hbparking.vagadegaragem;

import br.com.hbparking.colaborador.CannotFindColaborador;
import br.com.hbparking.colaborador.Colaborador;
import br.com.hbparking.colaborador.ColaboradorService;
import br.com.hbparking.colaborador.NoConnectionAPIException;
import br.com.hbparking.cor.Color;
import br.com.hbparking.marcas.Marca;
import br.com.hbparking.marcas.MarcaService;
import br.com.hbparking.periodo.Periodo;
import br.com.hbparking.periodo.PeriodoService;
import br.com.hbparking.security.jwt.JwtProvider;
import br.com.hbparking.security.jwt.TokenNotFoundException;
import br.com.hbparking.tipoveiculo.VehicleType;
import br.com.hbparking.vehicleModel.VehicleModel;
import br.com.hbparking.vehicleModel.VehicleModelService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VagaGaragemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VagaGaragemService.class);

    private final IVagaGaragemRepository iVagaGaragemRepository;
    private final MarcaService marcaService;
    private final VehicleModelService vehicleModelService;
    private final PeriodoService periodoService;
    private ValidadeOnHBEmployee validadeOnHBEmployee;
    private final ColaboradorService colaboradorService;
    private final JwtProvider jwtProvider;
    private static final String ID_INEXISTENTE = "ID não existe: ";

    public VagaGaragemService(IVagaGaragemRepository iVagaGaragemRepository, MarcaService marcaService,
                              VehicleModelService vehicleModelService, PeriodoService periodoService,
                              ValidadeOnHBEmployee validadeOnHBEmployee, ColaboradorService colaboradorService,
                              JwtProvider jwtProvider) {
        this.iVagaGaragemRepository = iVagaGaragemRepository;
        this.marcaService = marcaService;
        this.vehicleModelService = vehicleModelService;
        this.periodoService = periodoService;
        this.validadeOnHBEmployee = validadeOnHBEmployee;
        this.colaboradorService = colaboradorService;
        this.jwtProvider = jwtProvider;
    }

    public VagaGaragemDTO save(VagaGaragemDTO vagaGaragemDTO, HttpServletRequest request) throws CannotFindColaborador, TokenNotFoundException, InvalidVehicleTipoFromPeriodo, NoConnectionAPIException, InvalidVagaViolation {
        this.validate(vagaGaragemDTO);
        LOGGER.info("Salvando Vaga");
        LOGGER.debug("Vaga: {}", vagaGaragemDTO);
        VagaGaragem vagaSave = this.dtoToVaga(vagaGaragemDTO, request);
        vagaSave.setStatusVaga(StatusVaga.EMAPROVACAO);
        if (isCarroOrMoto(vagaSave.getTipoVeiculo())) {
            vagaSave.setPlaca(placaValidator(vagaGaragemDTO.getPlaca()));
        } else {
            vagaSave.setMarca(null);
            vagaSave.setPlaca(null);
            vagaSave.setVehicleModel(null);
            vagaSave.setColor(null);
        }
        validateTipoPeriodo(vagaSave);


        ResponseHBEmployeeDTO response = validadeOnHBEmployee.validate("http://localhost:8090/api/teste");

        boolean respostaDaApi = response.getParkingValid();
        if (respostaDaApi) {
            try {
                vagaSave = this.iVagaGaragemRepository.save(vagaSave);
            } catch (DataIntegrityViolationException e) {
                throw new DataIntegrityViolationException("A placa informada já está cadastrada no sistema");
            } catch (Exception ex) {
                throw new InvalidVagaViolation("Erro ao salvar vaga de garagem ", ex);
            }
        }


        return VagaGaragemDTO.of(vagaSave);
    }

    public Page<VagaGaragem> findAllByTipoPage(Pageable pageable) {
        LOGGER.info("Retornando vagas em paginas");
        return iVagaGaragemRepository.findAll(pageable);
    }

    public VagaGaragem findById(Long id) {
        Optional<VagaGaragem> vaga = this.iVagaGaragemRepository.findById(id);
        if (vaga.isPresent()) {
            return vaga.get();
        }
        throw new IllegalArgumentException(String.format(ID_INEXISTENTE, id));
    }

    public VagaGaragemDTO update(VagaGaragemDTO vagaGaragemDTO, Long id, HttpServletRequest request) throws TokenNotFoundException, CannotFindColaborador, CannotFindVaga {
        VagaGaragem vagaExsitente = this.iVagaGaragemRepository.findById(id).orElseThrow(() -> new CannotFindVaga(ID_INEXISTENTE + id));
        validate(vagaGaragemDTO);
        LOGGER.info("Atualizando vaga... id: [{}]", vagaExsitente.getId());
        LOGGER.debug("Payload: {}", vagaGaragemDTO);
        LOGGER.debug("Vaga Existente: {}", vagaExsitente);
        if (isCarroOrMoto(vagaExsitente.getTipoVeiculo())) {
            vagaExsitente.setPlaca(placaValidator(vagaGaragemDTO.getPlaca()));
        } else {
            vagaExsitente.setMarca(null);
            vagaExsitente.setPlaca(null);
            vagaExsitente.setVehicleModel(null);
        }
        this.iVagaGaragemRepository.save(vagaExsitente);
        vagaExsitente = this.dtoToVaga(vagaGaragemDTO, request);
        return VagaGaragemDTO.of(vagaExsitente);
    }

    public void validate(VagaGaragemDTO vagaGaragemDTO) {
        LOGGER.info("Validando Vaga");
        if (vagaGaragemDTO == null) {
            throw new IllegalArgumentException("VagaDTO não deve ser nulo");
        }
        if (vagaGaragemDTO.getPeriodo() == null) {
            throw new IllegalArgumentException("Periodo não deve ser nulo/vazio");
        }
        if (isCarroOrMoto(vagaGaragemDTO.getTipoVeiculo())) {
            if (vagaGaragemDTO.getMarca() == null) {
                throw new NullPointerException("Marca não deve ser nulo/vazio");
            }
            if (vagaGaragemDTO.getVehicleModel() == null) {
                throw new NullPointerException("Modelo não deve ser nulo/vazio");
            }
            if (vagaGaragemDTO.getColor() == null) {
                throw new NullPointerException("Cor não deve ser nulo/vazio");
            }
            if (!EnumUtils.isValidEnum(Color.class, vagaGaragemDTO.getColor().getDescricao())) {
                throw new IllegalArgumentException("Cor inválida");
            }
            if (vagaGaragemDTO.getPlaca() == null) {
                throw new NullPointerException("Placa não deve ser nulo/vazio");
            }
        }
    }

    private VagaGaragem dtoToVaga(VagaGaragemDTO vagaGaragemDTO, HttpServletRequest request) throws TokenNotFoundException, CannotFindColaborador {
        Marca marca = new Marca();
        VehicleModel modelo = new VehicleModel();
        Color cor = null;
        if (isCarroOrMoto(vagaGaragemDTO.getTipoVeiculo())) {
            marca = marcaService.findById(vagaGaragemDTO.getMarca());
            modelo = vehicleModelService.findById(vagaGaragemDTO.getVehicleModel());
            cor = vagaGaragemDTO.getColor();
        }
        Periodo periodo = periodoService.findById(vagaGaragemDTO.getPeriodo());
        Colaborador colaborador = getColaboradorLogado(request);
        return new VagaGaragem(
                vagaGaragemDTO.getTipoVeiculo(),
                marca,
                modelo,
                cor,
                vagaGaragemDTO.getPlaca(),
                periodo,
                colaborador,
                vagaGaragemDTO.getStatusVaga()
        );
    }


    public void delete(Long id) {
        LOGGER.info("Executando delete para vaga de ID: [{}]", id);
        this.iVagaGaragemRepository.deleteById(id);
    }

    public String placaValidator(String placa) {
        placa = placa.replaceAll("[^a-zA-Z0-9]", "");
        placa = placa.toUpperCase();
        Pattern pattern = Pattern.compile("[A-Z]{2,3}[0-9]{4}|[A-Z]{3,4}[0-9]{3}|[A-Z0-9]{7}");
        Matcher mat = pattern.matcher(placa);
        if (placa.length() > 0 && placa.length() < 8) {
            if (mat.matches()) {
                return placa;
            } else {
                throw new IllegalArgumentException("A placa informada não está no formato aceitável");
            }
        } else {
            throw new IllegalArgumentException("A placa informada não está no formato aceitável");
        }
    }

    private boolean isCarroOrMoto(VehicleType vehicleType) {

        return VehicleType.CARRO == vehicleType || VehicleType.MOTO == vehicleType;

    }

    public VagaGaragemDTO changeStatusVaga(Long id, StatusVaga statusVaga) {

        Optional<VagaGaragem> vagaGaragemOptional = this.iVagaGaragemRepository.findById(id);

        if (vagaGaragemOptional.isPresent()) {
            VagaGaragem vagaExsitente = vagaGaragemOptional.get();
            vagaExsitente.setStatusVaga(statusVaga);
            this.iVagaGaragemRepository.save(vagaExsitente);
            return VagaGaragemDTO.of(vagaExsitente);
        }
        throw new IllegalArgumentException(String.format(ID_INEXISTENTE, id));
    }

    public void validateTipoPeriodo(VagaGaragem vagaGaragem) throws InvalidVehicleTipoFromPeriodo {
        if (vagaGaragem.getPeriodo().getTipoVeiculo() != vagaGaragem.getTipoVeiculo()) {
            throw new InvalidVehicleTipoFromPeriodo("Periodo inválido para o tipo de veiculo desejado");
        }
    }

    public Colaborador getColaboradorLogado(HttpServletRequest request) throws TokenNotFoundException, CannotFindColaborador {
        String token = this.jwtProvider.getJwt(request);
        if (token != null && !token.isEmpty()) {
            String emailFromUserAuthenticated = this.jwtProvider.getEmailFromUserAuthenticated(token);
            return this.colaboradorService.findByEmail(emailFromUserAuthenticated);
        }
        throw new TokenNotFoundException("Colaborador não foi encontrado.");
    }

}