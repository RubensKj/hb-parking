package br.com.hbparking.csvColaborador;

import br.com.hbparking.colaborador.Colaborador;
import br.com.hbparking.colaborador.ColaboradorService;
import br.com.hbparking.util.ReadFileCSV;
import br.com.hbparking.vehicleException.ContentDispositionException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
public class ColaboradorImport {
    private final ReadFileCSV readFileCSV;
    private final ColaboradorService colaboradorService;

    public ColaboradorImport(ReadFileCSV readFileCSV, ColaboradorService colaboradorService) {
        this.readFileCSV = readFileCSV;
        this.colaboradorService = colaboradorService;
    }


}
