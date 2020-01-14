package br.com.hbparking.vagadegaragem;

import br.com.hbparking.colaborador.Colaborador;
import br.com.hbparking.colaborador.ColaboradorService;
import br.com.hbparking.colaborador.NoConnectionAPIException;
import br.com.hbparking.cor.Color;
import br.com.hbparking.email.MailSenderService;
import br.com.hbparking.marcas.Marca;
import br.com.hbparking.marcas.MarcaService;
import br.com.hbparking.periodo.Periodo;
import br.com.hbparking.periodo.PeriodoDTO;
import br.com.hbparking.periodo.PeriodoService;
import br.com.hbparking.periodo.PeriodosNotFoundException;
import br.com.hbparking.tipoveiculo.VehicleType;
import br.com.hbparking.vagainfo.*;
import br.com.hbparking.vehiclemodel.VehicleModel;
import br.com.hbparking.vehiclemodel.VehicleModelService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static br.com.hbparking.util.PlacaUtils.transformPlate;
import static br.com.hbparking.util.PlacaUtils.validatePlate;

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

    public VagaGaragemDTO save(VagaGaragemDTO vagaGaragemDTO) throws NoConnectionAPIException, InvalidVagaViolation, PlateAlreadyExistsException, InvalidPlatePatternException {
        this.validate(vagaGaragemDTO);
        validatePlate(vagaGaragemDTO.getPlaca());
        LOGGER.info("Salvando Vaga");
        LOGGER.debug("Vaga: {}", vagaGaragemDTO);
        vagaGaragemDTO.setStatusVaga(StatusVaga.EMAPROVACAO);
        VagaGaragem vagaSave = this.dtoToVaga(vagaGaragemDTO);
        if (isCarroOrMoto(vagaSave.getTipoVeiculo())) {
            vagaSave.setPlaca(transformPlate(vagaGaragemDTO.getPlaca()));
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
                throw new PlateAlreadyExistsException("A placa informada já está cadastrada no sistema");
            } catch (Exception ex) {
                LOGGER.info(ex.getMessage());
                throw new InvalidVagaViolation("Erro ao salvar vaga de garagem ", ex);
            }
        }

        return VagaGaragemDTO.of(vagaSave);
    }

    public Page<VagaGaragem> findAllByTipoPage(VehicleType vehicleType, Pageable pageable) {
        LOGGER.info("Retornando vagas em paginas");
        return iVagaGaragemRepository.findAllByTipoVeiculo(vehicleType, pageable);
    }

    public VagaGaragem findById(Long id) {
        Optional<VagaGaragem> vaga = this.iVagaGaragemRepository.findById(id);
        if (vaga.isPresent()) {
            return vaga.get();
        }
        throw new IllegalArgumentException(String.format(ID_INEXISTENTE, id));
    }

    public VagaGaragemDTO update(VagaGaragemDTO vagaGaragemDTO, Long id) throws InvalidPlatePatternException {
        Optional<VagaGaragem> vagaGaragemOptional = this.iVagaGaragemRepository.findById(id);
        if (vagaGaragemOptional.isPresent()) {
            VagaGaragem vagaExsitente = vagaGaragemOptional.get();
            validate(vagaGaragemDTO);
            validatePlate(vagaGaragemDTO.getPlaca());
            LOGGER.info("Atualizando vaga... id: [{}]", vagaExsitente.getId());
            LOGGER.debug("Payload: {}", vagaGaragemDTO);
            LOGGER.debug("Vaga Existente: {}", vagaExsitente);
            if (isCarroOrMoto(vagaExsitente.getTipoVeiculo())) {
                vagaExsitente.setPlaca(transformPlate(vagaGaragemDTO.getPlaca()));
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

    public List<VagaGaragem> sorteioVagas(Long periodoId, String tipoVeiculo, Turno turno) throws VagaInfoNotFoundException {
        int qtdVagas = 0;
        Periodo periodo = this.periodoService.findById(periodoId);
        VagaInfo vagaInfo = this.vagaInfoService.findByPeriodoAndVehicleTypeAndTurno(periodo, VehicleType.valueOf(tipoVeiculo), turno);
        qtdVagas = vagaInfo.getQuantidade();

        List<VagaGaragem> vagaGaragemList = this.iVagaGaragemRepository.findAllByStatusVagaAndTipoVeiculo(StatusVaga.EMAPROVACAO, VehicleType.valueOf(tipoVeiculo));

        List<VagaGaragem> vagasSorteadas = this.selectPrioritarios(vagaGaragemList);

        while (qtdVagas > vagasSorteadas.size() || vagaGaragemList.size() != vagasSorteadas.size()) {
            vagasSorteadas.add(vagaGaragemList.get(sorteio.nextInt(vagaGaragemList.size())));
            if (turno.getDescricao().equalsIgnoreCase("NOTURNO")) {
                vagasSorteadas.addAll(vagaGaragemList.stream().filter(vagaGaragem -> vagaGaragem.getColaborador().isTrabalhoNoturno()).collect(Collectors.toList()));
            }
            vagasSorteadas = vagasSorteadas.stream().distinct().sorted(Comparator.comparing((VagaGaragem::getPlaca))).collect(Collectors.toList());
        }

        for (VagaGaragem vaga : vagasSorteadas) {
            this.approveVaga(VagaGaragemDTO.of(vaga), turno);
            LOGGER.debug("Update vaga aprovada: {}", vaga.getId());
        }

        List<VagaGaragem> listaReprovados = this.iVagaGaragemRepository.findAllByStatusVagaAndTipoVeiculo(StatusVaga.EMAPROVACAO, VehicleType.valueOf(tipoVeiculo));
        for (VagaGaragem reprovado : listaReprovados) {
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

        this.vagaInfoService.update(VagaInfoDTO.of(vagaInfo), vagaGaragemDTO.getPeriodo());

        return this.changeStatusVaga(vagaGaragemDTO.getId(), StatusVaga.APROVADA);
    }

    public List<VagaGaragemDTO> approveAllVagas(List<VagaGaragem> vagaGaragemList, Turno turno) {
        List<VagaGaragemDTO> vagaGaragemsSucceed = new ArrayList<>();
        vagaGaragemList.forEach(vagaGaragem -> {
            try {
                VagaGaragemDTO vagaGaragemDTO = this.approveVaga(VagaGaragemDTO.of(vagaGaragem), turno);
                vagaGaragemsSucceed.add(vagaGaragemDTO);
            } catch (VagaInfoNotFoundException e) {
                LOGGER.error(e.getMessage());
            }
        });

        return vagaGaragemsSucceed;
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

    /*Remover esse método após uso*/
    @Transactional
    public void importRemoverDepois(MultipartFile arquivo) throws IOException {

        String separador = ";";
        String linha = "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(arquivo.getInputStream()))) {

            linha = br.readLine();
            while ((linha = br.readLine()) != null) {
                try {
                    String[] resultadoSplit = linha.split(separador);

                    VagaGaragemDTO vagaGaragemDTO = new VagaGaragemDTO();

                    vagaGaragemDTO.setTipoVeiculo(VehicleType.valueOf(resultadoSplit[0]));

//                    Marca marca = this.marcaService.findById(Long.parseLong(resultadoSplit[1]));
//                    vagaGaragemDTO.setMarca(marca.getId());

                    VehicleModel vehicleModel = this.vehicleModelService.findByModelo(resultadoSplit[2]);
                    vagaGaragemDTO.setVehicleModel(vehicleModel.getId());
                    vagaGaragemDTO.setMarca(vehicleModel.getFkMarca().getId());

                    vagaGaragemDTO.setColor(Color.valueOf(resultadoSplit[3]));

                    vagaGaragemDTO.setPlaca(resultadoSplit[4]);

                    Periodo periodo = this.periodoService.findById(Long.parseLong(resultadoSplit[5]));
                    vagaGaragemDTO.setPeriodo(periodo.getId());

                    Colaborador colaborador = this.colaboradorService.findById(Long.parseLong(resultadoSplit[6]));
                    vagaGaragemDTO.setColaborador(colaborador.getId());

                    this.save(vagaGaragemDTO);
                } catch (Exception e) {
                    LOGGER.error("Erro: {}", e.getStackTrace());
                }
            }
        }
    }

    public int getTotalElementsFilter(VehicleType vehicleType, boolean trabalhoNoturno, Long idPeriodo, StatusVaga statusVaga) {
        return this.iVagaGaragemRepository.findAllByTipoVeiculoAndColaborador_TrabalhoNoturnoAndPeriodo_IdAndStatusVaga(vehicleType, trabalhoNoturno, idPeriodo, statusVaga).size();
    }

    public Page<VagaGaragem> findByPeriodoAndStatusVagaAndVehicleType(Periodo periodo) {
        return this.iVagaGaragemRepository.findByPeriodoAndStatusVagaAndTipoVeiculo(periodo, StatusVaga.EMAPROVACAO, periodo.getTipoVeiculo(), PageRequest.of(0, 10));
    }

    public VagasContent getVagasContent(VehicleType vehicleType, int page, int limit, Turno turno) throws PeriodosNotFoundException, VagaInfoNotFoundException {
        List<PeriodoDTO> periodoByVehicleType = this.periodoService.findPeriodoByVehicleType(vehicleType);
        PeriodoDTO periodoDTO = this.periodoService.findAnyInsideList(periodoByVehicleType);
        Periodo periodo = this.periodoService.findById(periodoDTO.getId());

        Pageable pageable = PageRequest.of(page, limit);

        Page<VagaGaragem> vagasGaragem = this.iVagaGaragemRepository.findByPeriodoAndStatusVagaAndTipoVeiculo(periodo, StatusVaga.EMAPROVACAO, vehicleType, pageable);
        VagaInfo vagaInfo = this.vagaInfoService.findByPeriodoAndVehicleTypeAndTurno(periodo, vehicleType, turno);

        return new VagasContent(vagasGaragem, periodo, vagaInfo);
    }
}
