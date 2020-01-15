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
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private final VagaGaragemHistoryService vagaGaragemHistoryService;
    private final VagaGaragemHistoryRepository vagaGaragemHistoryRepository;



    public VagaGaragemDTO save(VagaGaragemDTO vagaGaragemDTO) throws NoConnectionAPIException, ColaboradorAlreadyExistsInPeriodoException, InvalidPlatePatternException, InvalidVehicleTipoFromPeriodo {
        this.validate(vagaGaragemDTO);
        LOGGER.info("Salvando Vaga");
        LOGGER.debug("Vaga: {}", vagaGaragemDTO);
        vagaGaragemDTO.setStatusVaga(StatusVaga.EMAPROVACAO);
        VagaGaragem vagaSave = this.dtoToVaga(vagaGaragemDTO);
        validateTipoPeriodo(vagaSave);

        if (validadeOnHBEmployee.validate("http://localhost:8090/api/teste").getParkingValid()) {
            VagaGaragem savedVaga = this.iVagaGaragemRepository.save(vagaSave);
            this.vagaGaragemHistoryService.saveData(vagaSave, this.colaboradorService.getPriorityColaborador(vagaSave.getColaborador()));
            return VagaGaragemDTO.of(savedVaga);
        }
        throw new ColaboradorAlreadyExistsInPeriodoException("Colaborador já cadastrado neste período com esta placa.");
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
            this.vagaGaragemHistoryService.saveUpdateAction(vagaExsitente, "ALTERACAO", this.colaboradorService.getPriorityColaborador(vagaExsitente.getColaborador()));
            vagaExsitente = this.dtoToVaga(vagaGaragemDTO);
            return VagaGaragemDTO.of(vagaExsitente);
        }
        throw new IllegalArgumentException(String.format(ID_INEXISTENTE, id));
    }

    public void validate(VagaGaragemDTO vagaGaragemDTO) throws InvalidPlatePatternException {
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
            validatePlate(vagaGaragemDTO.getPlaca());
        }
    }

    private VagaGaragem dtoToVaga(VagaGaragemDTO vagaGaragemDTO) {
        Marca marca = new Marca();
        VehicleModel modelo = new VehicleModel();

        if (isCarroOrMoto(vagaGaragemDTO.getTipoVeiculo())) {
            marca = marcaService.findById(vagaGaragemDTO.getMarca());
            modelo = vehicleModelService.findById(vagaGaragemDTO.getVehicleModel());
        } else {
            marca = null;
            modelo = null;
        }
        Periodo periodo = periodoService.findById(vagaGaragemDTO.getPeriodo());
        Colaborador colaborador = colaboradorService.findById(vagaGaragemDTO.getColaborador());
        return new VagaGaragem(
                vagaGaragemDTO.getTipoVeiculo(),
                marca,
                modelo,
                vagaGaragemDTO.getColor(),
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
                    this.vagaGaragemHistoryService.saveUpdateAction(vagaExsitente, "APROVACAO", this.colaboradorService.getPriorityColaborador(vagaExsitente.getColaborador()));
                }
                if (statusVaga.getDescricao().equalsIgnoreCase("REPROVADO")) {
                    this.mailSenderService.sendEmailDisapproved(vagaExsitente);
                    this.vagaGaragemHistoryService.saveUpdateAction(vagaExsitente, "REPROVACAO", this.colaboradorService.getPriorityColaborador(vagaExsitente.getColaborador()));
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
            vagasSorteadas = vagasSorteadas.stream().distinct().sorted(Comparator.comparing((vagaGaragem -> vagaGaragem.getColaborador().getEmail()))).collect(Collectors.toList());
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

    public VagasContent sort(Long periodoId, String tipoVeiculo, Turno turno) throws VagaInfoNotFoundException {
        Map<VagaGaragem, Integer> scoredMap = this.colaboradorService.getMapColaboradorAndPriority(this.iVagaGaragemRepository.findAllByStatusVagaAndTipoVeiculo(StatusVaga.EMAPROVACAO, VehicleType.valueOf(tipoVeiculo)));
        List<VagaPriority> vagaPriorityList = new ArrayList<>();
        List<Integer> priorityList = new ArrayList<>();

        scoredMap.forEach((vagaGaragem, priority) -> vagaPriorityList.add(new VagaPriority(vagaGaragem, priority)));

        vagaPriorityList.forEach(vagaPriority -> priorityList.add(vagaPriority.getPriority()));

        int minimalIndex = priorityList.indexOf(Collections.min(priorityList));

        for (int i = 0; i < vagaPriorityList.size(); i++) {
            if (i != minimalIndex) {
                VagaGaragem vagaGaragem = vagaPriorityList.get(i).getVagaGaragem();
                vagaGaragem.setStatusVaga(StatusVaga.APROVADA);
                this.iVagaGaragemRepository.save(vagaGaragem);
                vagaPriorityList.remove(vagaPriorityList.get(i));
            } else {
                VagaInfo vagaInfo = this.vagaInfoService.findByPeriodoAndVehicleTypeAndTurno(this.periodoService.findById(periodoId), VehicleType.valueOf(tipoVeiculo), turno);
                this.finalSorting(vagaPriorityList, vagaInfo);
            }
        }

        Periodo periodo = this.periodoService.findById(periodoId);
        VehicleType vehicleType = VehicleType.valueOf(tipoVeiculo.toUpperCase());
        VagaInfo vagaInfo = this.vagaInfoService.findByPeriodoAndVehicleTypeAndTurno(periodo, vehicleType, turno);
        Page<VagaGaragem> pageVagaGaragem = this.iVagaGaragemRepository.findByPeriodoAndStatusVagaAndTipoVeiculo(periodo, StatusVaga.APROVADA, vehicleType, PageRequest.of(0, 10));
//        new VagasContent(pageVagaGaragem, PeriodoDTO.of(periodo), VagaInfoDTO.of(vagaInfo))
        return null;
    }

    public void finalSorting(List<VagaPriority> vagaPriorityList, VagaInfo vagaInfo) throws VagaInfoNotFoundException {
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < vagaInfo.getQuantidade(); i++) {
            VagaGaragem vagaGaragem = vagaPriorityList.get(random.nextInt(vagaPriorityList.size())).getVagaGaragem();
            vagaGaragem.setStatusVaga(StatusVaga.APROVADA);
            this.iVagaGaragemRepository.save(vagaGaragem);
            vagaInfo.setQuantidade(vagaInfo.getQuantidade() - 1);
        }
        this.vagaInfoService.update(VagaInfoDTO.of(vagaInfo), vagaInfo.getId());
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

        Periodo periodo = this.periodoService.findById(vagaGaragemDTO.getPeriodo());
        VagaInfo vagaInfo = this.vagaInfoService.findByPeriodoAndVehicleTypeAndTurno(periodo, vagaGaragemDTO.getTipoVeiculo(), turno);
        vagaInfo.setQuantidade(updateNumberOfVagasLeft(vagaInfo.getQuantidade()));

        this.vagaInfoService.update(VagaInfoDTO.of(vagaInfo), vagaGaragemDTO.getPeriodo());
        List<VagaGaragemDTO> placasDuplicadas = this.iVagaGaragemRepository.findAllByPlacaAndPeriodoAndStatusVaga(vagaGaragemDTO.getPlaca(), vagaInfo.getPeriodo(), StatusVaga.EMAPROVACAO);
        placasDuplicadas.remove(vagaGaragemDTO);

        for (VagaGaragemDTO duplicada : placasDuplicadas) {
            this.changeStatusVaga(duplicada.getId(), StatusVaga.REPROVADO);
        }

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
