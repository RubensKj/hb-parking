package br.com.hbparking.vagadegaragem;

import br.com.hbparking.periodo.Periodo;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

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

    public VagaGaragemHistoryService(VagaGaragemHistoryRepository vagaGaragemHistoryRepository) {
        this.vagaGaragemHistoryRepository = vagaGaragemHistoryRepository;
    }

    public void saveData(VagaGaragem vagaGaragem) {
        this.vagaGaragemHistoryRepository.save(new VagaGaragemHistory(vagaGaragem, 0, LocalDateTime.now(Clock.systemUTC()), "CRIACAO"));
    }

    public void saveUpdateAction(VagaGaragem vagaGaragem, String message) {
        this.vagaGaragemHistoryRepository.save(new VagaGaragemHistory(vagaGaragem, 0, LocalDateTime.now(Clock.systemUTC()), message));
    }


    public void exportByColumns(HttpServletResponse response, Long idPeriodo, boolean marca, boolean email, boolean tipo, boolean cor) throws IOException {
        List<VagaGaragemHistory> completeList = this.vagaGaragemHistoryRepository.findAll();

        String filename = "relatorio_locatarios.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        PrintWriter writer = response.getWriter();
        writer.write(headerBuilder(marca, email, tipo, cor));

        for (String vagaExport : exportList(completeList, idPeriodo, marca, email, tipo, cor) ) {
            writer.write(vagaExport);
        }
        writer.close();
    }

    public List<String> exportList(List<VagaGaragemHistory> completeList, Long idPeriodo, boolean marca, boolean email, boolean tipo, boolean cor){

        List<VagaGaragemHistory> filterPeriodo = new ArrayList<>();
        List<String> exportList = new ArrayList<>();

        filterPeriodo.addAll(completeList.stream().filter(vagaGaragemHistory -> vagaGaragemHistory.getVagaGaragem().getPeriodo().getId().equals(idPeriodo)).collect(Collectors.toList()));

        for (VagaGaragemHistory history : filterPeriodo) {
            String linha = "";
            VagaGaragem vagaGaragem = history.getVagaGaragem();
            linha = (vagaGaragem.getColaborador().getNome() + ";");
            linha = (linha + vagaGaragem.getVehicleModel().getModelo() + ";");
            linha = (linha + vagaGaragem.getPlaca() + ";");
            linha = (linha + vagaGaragem.getStatusVaga() + ";");
            if(marca){
                linha = (linha + vagaGaragem.getMarca() + ";");
            }
            if(email){
                linha = (linha + vagaGaragem.getColaborador().getEmail() + ";");
            }
            if(tipo){
                linha = (linha + vagaGaragem.getVehicleModel() + ";");
            }
            if(cor){
                linha = (linha + vagaGaragem.getColor());
            }
            exportList.add(linha + "\n");
        }

        return exportList;
    }

    public String headerBuilder(boolean marca, boolean email, boolean tipo, boolean cor){
        String header = "NOME;MODELO;PLACA;STATUS;";
        if(marca){
            header = header + "MARCA;";
        }
        if(email){
            header = header + "E-MAIL;";
        }
        if(tipo){
            header = header + "TIPO_VEICULO;";
        }
        if(cor){
            header = header + "COR";
        }
        return (header +  "\n");
    }

}
