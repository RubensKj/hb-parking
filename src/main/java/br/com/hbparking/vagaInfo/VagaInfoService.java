package br.com.hbparking.vagaInfo;

import br.com.hbparking.periodo.Periodo;
import br.com.hbparking.periodo.PeriodoService;
import br.com.hbparking.tipoveiculo.VehicleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VagaInfoService {

    private final IVagaInfoRepository iVagaInfoRepository;

    private final PeriodoService periodoService;

    private static final Logger LOGGER = LoggerFactory.getLogger(VagaInfoService.class);

    @Autowired
    public VagaInfoService(IVagaInfoRepository iVagaInfoRepository, PeriodoService periodoService) {
        this.iVagaInfoRepository = iVagaInfoRepository;
        this.periodoService = periodoService;
    }

    public VagaInfoDTO cadastrar(VagaInfoDTO vagaInfoDTO) throws PeriodoAlreadyExistsException {
        LOGGER.info("Cadastrando informações de vaga");
        this.validarVagaInfo(vagaInfoDTO);

        Periodo periodo = periodoService.findById(vagaInfoDTO.getIdPeriodo());
        if (!this.iVagaInfoRepository.existsByPeriodoAndVehicleTypeAndTurno(periodo, vagaInfoDTO.getVehicleType(), vagaInfoDTO.getTurno())) {
            return VagaInfoDTO.of(this.iVagaInfoRepository.save(new VagaInfo(vagaInfoDTO.getQuantidade(), vagaInfoDTO.getValor(), vagaInfoDTO.getVehicleType(), periodo, vagaInfoDTO.getTurno())));
        }
        throw new PeriodoAlreadyExistsException("Periodo já cadastrado no banco");

    }

    public VagaInfo findById(Long id) throws VagaInfoNotFoundException {
        LOGGER.info("Validando vaga de ID [{}]", id);
        return this.iVagaInfoRepository.findById(id).orElseThrow(() -> new VagaInfoNotFoundException("Não foi encontrada uma informação de vaga com o ID"));
    }

    private void validarVagaInfo(VagaInfoDTO vagaInfoDTO) {
        LOGGER.info("Validando vaga");
        if (vagaInfoDTO.getQuantidade() < 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        if (vagaInfoDTO.getValor() < 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }
        if (vagaInfoDTO.getVehicleType().getDescricao().equalsIgnoreCase("bicicleta") || vagaInfoDTO.getVehicleType().getDescricao().equalsIgnoreCase("patinete")) {
            throw new IllegalArgumentException("Patinete e bicicleta são considerados o mesmo tipo para o cadastro de quantidade");
        }
    }

    public VagaInfo findByPeriodoAndVehicleTypeAndTurno(Periodo periodo, VehicleType vehicleType, Turno turno) throws VagaInfoNotFoundException {
        return this.iVagaInfoRepository.findByPeriodoAndVehicleTypeAndTurno(periodo, vehicleType, turno).orElseThrow(() -> new VagaInfoNotFoundException("Não foi encontrado nenhuma informação de vaga com este periodo"));
    }

    public VagaInfoDTO update(VagaInfoDTO vagaInfoDTO, Long id) throws VagaInfoNotFoundException {
        LOGGER.info("Atualizando vaga info");
        this.validarVagaInfo(vagaInfoDTO);
        VagaInfo vagaInfo = this.findById(id);
        vagaInfo.setQuantidade(vagaInfoDTO.getQuantidade());
        vagaInfo.setPeriodo(this.periodoService.findById(id));
        vagaInfo.setValor(vagaInfoDTO.getValor());
        vagaInfo.setVehicleType(vagaInfoDTO.getVehicleType());

        return VagaInfoDTO.of(this.iVagaInfoRepository.save(vagaInfo));
    }
}