package br.com.hbparking.vagaInfo;

import br.com.hbparking.periodo.PeriodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public VagaInfoDTO cadastrar(VagaInfoDTO vagaInfoDTO) {
        LOGGER.info("Cadastrando informações de vaga");
        this.validarVagaInfo(vagaInfoDTO);

        return VagaInfoDTO.of(this.iVagaInfoRepository.save(new VagaInfo(vagaInfoDTO.getQuantidade(), vagaInfoDTO.getValor(), vagaInfoDTO.getVehicleType(), periodoService.findById(vagaInfoDTO.getIdPeriodo()))));
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
}