package br.com.hbparking.vagadegaragem;

import br.com.hbparking.colaborador.Colaborador;
import br.com.hbparking.colaborador.ColaboradorService;
import br.com.hbparking.colaborador.NoConnectionAPIException;
import br.com.hbparking.cor.Color;
import br.com.hbparking.email.MailSenderService;
import br.com.hbparking.marcas.Marca;
import br.com.hbparking.marcas.MarcaService;
import br.com.hbparking.periodo.Periodo;
import br.com.hbparking.periodo.PeriodoService;
import br.com.hbparking.tipoveiculo.VehicleType;
import br.com.hbparking.vagaInfo.*;
import br.com.hbparking.vehicleModel.VehicleModel;
import br.com.hbparking.vehicleModel.VehicleModelService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.Optional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VagaGaragemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VagaGaragemService.class);

    private final IVagaGaragemRepository iVagaGaragemRepository;
    private final MarcaService marcaService;
    private final VehicleModelService vehicleModelService;
    private final PeriodoService periodoService;
    private ValidadeOnHBEmployee validadeOnHBEmployee;
    private final ColaboradorService colaboradorService;
    private final VagaInfoService vagaInfoService;
    private static final String ID_INEXISTENTE = "ID %s não existe";
    private final Random sorteio = new Random();
    private final MailSenderService mailSenderService;

    public VagaGaragemDTO save(VagaGaragemDTO vagaGaragemDTO) throws NoConnectionAPIException, InvalidVagaViolation {
        this.validate(vagaGaragemDTO);
        LOGGER.info("Salvando Vaga");
        LOGGER.debug("Vaga: {}", vagaGaragemDTO);
        vagaGaragemDTO.setStatusVaga(StatusVaga.EMAPROVACAO);
        VagaGaragem vagaSave = this.dtoToVaga(vagaGaragemDTO);

        if (isCarroOrMoto(vagaSave.getTipoVeiculo())) {
            vagaSave.setPlaca(placaValidator(vagaGaragemDTO.getPlaca()));
        } else {
            vagaSave.setMarca(null);
            vagaSave.setPlaca(null);
            vagaSave.setVehicleModel(null);
            vagaSave.setColor(null);
        }
        try {
            validateTipoPeriodo(vagaSave);
        } catch (InvalidVehicleTipoFromPeriodo invalidVehicleTipoFromPeriodo) {
            LOGGER.debug(String.valueOf(invalidVehicleTipoFromPeriodo));
        }

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

    public VagaGaragemDTO update(VagaGaragemDTO vagaGaragemDTO, Long id) {
        Optional<VagaGaragem> vagaGaragemOptional = this.iVagaGaragemRepository.findById(id);
        if (vagaGaragemOptional.isPresent()) {
            VagaGaragem vagaExsitente = vagaGaragemOptional.get();
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
            vagaExsitente = this.dtoToVaga(vagaGaragemDTO);
            return VagaGaragemDTO.of(vagaExsitente);
        }
        throw new IllegalArgumentException(String.format(ID_INEXISTENTE, id));
    }

    public void validate(VagaGaragemDTO vagaGaragemDTO) {
        LOGGER.info("Validando Vaga");
        if (vagaGaragemDTO == null) {
            throw new IllegalArgumentException("VagaDTO não deve ser nulo");
        }
        if (vagaGaragemDTO.getPeriodo() == null) {
            throw new IllegalArgumentException("Periodo não deve ser nulo/vazio");
        }
        if (vagaGaragemDTO.getColaborador() == null) {
            throw new IllegalArgumentException("Usuario não deve ser nulo/vazio");
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

    private VagaGaragem dtoToVaga(VagaGaragemDTO vagaGaragemDTO) {
        Marca marca = new Marca();
        VehicleModel modelo = new VehicleModel();
        Color cor = null;
        if (isCarroOrMoto(vagaGaragemDTO.getTipoVeiculo())) {
            marca = marcaService.findById(vagaGaragemDTO.getMarca());
            modelo = vehicleModelService.findById(vagaGaragemDTO.getVehicleModel());
            cor = vagaGaragemDTO.getColor();
        }
        Periodo periodo = periodoService.findById(vagaGaragemDTO.getPeriodo());
        Colaborador colaborador = colaboradorService.findById(vagaGaragemDTO.getColaborador());
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

            new Thread(() -> {
                if (statusVaga.getDescricao().equalsIgnoreCase("APROVADA")) {
                    this.mailSenderService.sendEmailSuccess(vagaExsitente);
                }
                if (statusVaga.getDescricao().equalsIgnoreCase("REPROVADO")) {
                    this.mailSenderService.sendEmailDisapproved(vagaExsitente);
                }
            }).start();
            return VagaGaragemDTO.of(vagaExsitente);
        }
        throw new IllegalArgumentException(String.format(ID_INEXISTENTE, id));
    }

    public void validateTipoPeriodo(VagaGaragem vagaGaragem) throws InvalidVehicleTipoFromPeriodo {
        if (vagaGaragem.getPeriodo().getTipoVeiculo() != vagaGaragem.getTipoVeiculo()) {
            throw new InvalidVehicleTipoFromPeriodo("Periodo inválido para o tipo de veiculo desejado");
        }
    }
  
    public List<VagaGaragem> sorteioVagas(int qtdVagas, String tipoVeiculo) {


        List<VagaGaragem> vagaGaragemList = this.iVagaGaragemRepository.findAllByStatusVagaAndTipoVeiculo(StatusVaga.EMAPROVACAO, VehicleType.valueOf(tipoVeiculo));

        List<VagaGaragem> vagasSorteadas = this.selectPrioritarios(vagaGaragemList);

        while (qtdVagas > vagasSorteadas.size() || vagaGaragemList.size() != vagasSorteadas.size()) {
            vagasSorteadas.add(vagaGaragemList.get(sorteio.nextInt(vagaGaragemList.size())));
            vagasSorteadas = vagasSorteadas.stream().distinct().sorted(Comparator.comparing((VagaGaragem::getPlaca))).collect(Collectors.toList());
        }
        for (VagaGaragem vaga: vagasSorteadas) {
            this.changeStatusVaga(vaga.getId(), StatusVaga.VIGENTE);
            LOGGER.debug("Update vaga aprovada: {}", vaga.getId());
        }

        List<VagaGaragem> listaReprovados = this.iVagaGaragemRepository.findAllByStatusVagaAndTipoVeiculo(StatusVaga.EMAPROVACAO, VehicleType.valueOf(tipoVeiculo));

        for(VagaGaragem reprovado : listaReprovados){
            this.changeStatusVaga(reprovado.getId(), StatusVaga.REPROVADO);
            LOGGER.debug("Update vaga reprovada: {}", reprovado.getId());
        }

        return vagasSorteadas;
    }

    public List<VagaGaragem> selectPrioritarios(List<VagaGaragem> vagaGaragemList) {

        List<VagaGaragem> vagaPrioritaria = new ArrayList<>();

        vagaPrioritaria.addAll(vagaGaragemList.stream().filter(vagaGaragem -> vagaGaragem.getColaborador().isPcd()).collect((Collectors.toList())));
        vagaPrioritaria.addAll(vagaGaragemList.stream().filter(vagaGaragem -> LocalDate.now().getYear() - vagaGaragem.getColaborador().getDataNascimento().getYear() >= 60).collect(Collectors.toList()));
        vagaPrioritaria.addAll(vagaGaragemList.stream().filter(vagaGaragem -> vagaGaragem.getColaborador().isResideForaBlumenau()).collect(Collectors.toList()));
        vagaPrioritaria.addAll(vagaGaragemList.stream().filter(vagaGaragem -> vagaGaragem.getColaborador().isOfereceCarona()).collect(Collectors.toList()));

        return vagaPrioritaria;
    }

    public VagaGaragemDTO approveVaga(VagaGaragemDTO vagaGaragemDTO, Turno turno) throws VagaInfoNotFoundException {

        VagaInfo vagaInfo = this.vagaInfoService.findByPeriodoAndVehicleTypeAndTurno(this.periodoService.findById(vagaGaragemDTO.getPeriodo()), vagaGaragemDTO.getTipoVeiculo(), turno);
        vagaInfo.setQuantidade(updateNumberOfVagasLeft(vagaInfo.getQuantidade()));

        this.vagaInfoService.update(VagaInfoDTO.of(vagaInfo), vagaInfo.getId());

        return this.changeStatusVaga(vagaGaragemDTO.getId(), StatusVaga.APROVADA);
    }

    public void approveAllVagas(List<VagaGaragemDTO> vagaGaragemDTOList, Turno turno) {
        vagaGaragemDTOList.forEach(vagaGaragemDTO -> {
            try {
                this.approveVaga(vagaGaragemDTO, turno);
            } catch (VagaInfoNotFoundException e) {
                LOGGER.error("", e);
            }
        });
    }

    public int updateNumberOfVagasLeft(int quantidade) {

        if (quantidade > 0) {
            return quantidade - 1;
        }
        throw new IllegalArgumentException("Todas as vagas já foram preenchidas");
    }

    public void exportVagaGaragemCSVfromPeriodo(Long idPeriodo, HttpServletResponse response) throws IOException, VagaInfoNotFoundException {
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=teste.csv");

        List<VagaGaragem> vagaGaragemList = this.iVagaGaragemRepository.findByPeriodo(this.periodoService.findById(idPeriodo));
        PrintWriter writer = response.getWriter();
        double valor = 0;
        writer.write("Nome;E-mail;Tipo Veiculo;Marca;Modelo;Cor;Placa;Status;Valor\n");
        for (VagaGaragem vagaGaragem : vagaGaragemList) {
            if (vagaGaragem.getColaborador().isTrabalhoNoturno()) {
                valor = this.vagaInfoService.findByPeriodoAndVehicleTypeAndTurno(vagaGaragem.getPeriodo(), vagaGaragem.getTipoVeiculo(), Turno.NOTURNO).getValor();
            } else {
                valor = this.vagaInfoService.findByPeriodoAndVehicleTypeAndTurno(vagaGaragem.getPeriodo(), vagaGaragem.getTipoVeiculo(), Turno.INTEGRAL).getValor();
            }
            writer.write(vagaGaragem.getColaborador().getNome() + ";" + vagaGaragem.getColaborador().getEmail() + ";"
                    + vagaGaragem.getTipoVeiculo() + ";" + vagaGaragem.getMarca().getNome() + ";" + vagaGaragem.getVehicleModel().getModelo() + ";"
                    + vagaGaragem.getColor() + ";" + vagaGaragem.getPlaca() + ";" + vagaGaragem.getStatusVaga() + ";" + valor + ";" + "\n"
            );
        }
        writer.close();
    }

    public Page<VagaGaragem> findAllFromPeriodo(Long idPeriodo, Pageable pageable) {
        return this.iVagaGaragemRepository.findByPeriodo(this.periodoService.findById(idPeriodo), pageable);
    }
}
