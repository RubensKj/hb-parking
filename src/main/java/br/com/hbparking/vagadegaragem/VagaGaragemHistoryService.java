package br.com.hbparking.vagadegaragem;

import br.com.hbparking.periodo.PeriodoService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VagaGaragemHistoryService {

    private final VagaGaragemHistoryRepository vagaGaragemHistoryRepository;
    private final PeriodoService periodoService;
    private final String FILE_NAME = "relatorio_locatarios";

    public VagaGaragemHistoryService(VagaGaragemHistoryRepository vagaGaragemHistoryRepository, PeriodoService periodoService) {
        this.vagaGaragemHistoryRepository = vagaGaragemHistoryRepository;
        this.periodoService = periodoService;


    public void saveData(VagaGaragem vagaGaragem, Integer prioridade) {
        this.IVagaGaragemHistoryRepository.save(new VagaGaragemHistory(vagaGaragem, prioridade, LocalDateTime.now(Clock.systemUTC()), "CRIACAO"));
    }

    public void saveUpdateAction(VagaGaragem vagaGaragem, String message, Integer prioridade) {
        this.IVagaGaragemHistoryRepository.save(new VagaGaragemHistory(vagaGaragem, prioridade, LocalDateTime.now(Clock.systemUTC()), message));
    }


    public void exportByColumns(HttpServletResponse response, Long idPeriodo, boolean marca, boolean email, boolean tipo, boolean cor) {

        List<VagaGaragemHistory> completeList = this.vagaGaragemHistoryRepository.findByVagaGaragem_Periodo(periodoService.findById(idPeriodo));

        if(completeList.isEmpty()){
            throw new NoResultException("Nenhum registro localizado.");
        }

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + FILE_NAME + "\"" + ".csv");

        try(PrintWriter writer = response.getWriter()) {
            writer.write(headerBuilder(marca, email, tipo, cor));
            for(String exportLine : exportList(completeList, marca, email, tipo, cor)){
                writer.print(exportLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public List<String> exportList(List<VagaGaragemHistory> completeList, boolean marca, boolean email, boolean tipo, boolean cor){

        List<String> exportList = new ArrayList<>();

        for (VagaGaragemHistory history : completeList) {
            String linha = "";
            VagaGaragem vagaGaragem = history.getVagaGaragem();
            linha = (vagaGaragem.getColaborador().getNome() + ";");
            linha = linha.concat(vagaGaragem.getVehicleModel().getModelo() + ";");
            linha = linha.concat(vagaGaragem.getPlaca() + ";");
            linha = linha.concat(vagaGaragem.getStatusVaga() + ";");
            if(marca){
                linha = linha.concat(vagaGaragem.getMarca().getNome() + ";");
            }
            if(email){
                linha = linha.concat(vagaGaragem.getColaborador().getEmail() + ";");
            }
            if(tipo){
                linha = linha.concat(vagaGaragem.getTipoVeiculo() + ";");
            }
            if(cor){
                linha = linha.concat(vagaGaragem.getColor().toString());
            }
            exportList.add(linha + "\n");
        }

        return exportList;
    }

    public String headerBuilder(boolean marca, boolean email, boolean tipo, boolean cor){
        String header = "NOME;MODELO;PLACA;STATUS;";
        if(marca){
            header = header.concat("MARCA;");
        }
        if(email){
            header = header.concat("E-MAIL;");
        }
        if(tipo){
            header = header.concat("TIPO_VEICULO;");
        }
        if(cor){
            header = header.concat("COR");
        }
        return (header +  "\n");
    }

}
