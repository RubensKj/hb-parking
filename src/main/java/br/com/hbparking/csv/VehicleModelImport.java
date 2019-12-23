package br.com.hbparking.csv;

import br.com.hbparking.marcas.CannotFindAnyMarcaWithId;
import br.com.hbparking.marcas.Marca;
import br.com.hbparking.marcas.MarcaService;
import br.com.hbparking.vehicleException.ContentDispositionException;
import br.com.hbparking.vehicleModel.VehicleModel;
import br.com.hbparking.vehicleModel.VehicleModelService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class VehicleModelImport {
    private final VehicleModelService vehicleModelService;
    private final MarcaService marcaService;

    public VehicleModelImport(VehicleModelService vehicleModelService, MarcaService marcaService) {
        this.vehicleModelService = vehicleModelService;
        this.marcaService = marcaService;
    }

    @Transactional
    public void importVehicle(MultipartFile multipartFile, HttpServletResponse response) throws Exception {
        List<String[]> dataArray = this.readFile(multipartFile);
        List<VehicleModel> vehicleModelList = new ArrayList<>();

        dataArray.forEach(lineData -> {
            for (int i = 0; i < lineData.length; i += 3) {
                if (!(lineData[i].isEmpty()) && !(lineData[i + 1].isEmpty())) {

                    Marca marca;
                    try {
                        marca = this.marcaService.findEntityById(new Long(lineData[i]));
                    } catch (CannotFindAnyMarcaWithId cannotFindAnyMarcaWithId) {
                        return;
                    }

                    VehicleModel vehicleModel = new VehicleModel();
                    vehicleModel.setFkMarca(marca);
                    vehicleModel.setModelo(lineData[i + 1]);
                    vehicleModelList.add(vehicleModel);
                }
            }
        });

        //verify if entries have been deleted
        this.hasBeenDeleted(vehicleModelList);

        vehicleModelList.stream().forEach(model -> {
            if (!this.vehicleModelService.exists(model)) {
                this.vehicleModelService.saveVehicle(model);
            }
        });
    }

    public void hasBeenDeleted(List<VehicleModel> csvList) {
        List<String> nomes = csvList.stream().map(VehicleModel::getModelo).collect(Collectors.toList());
        this.vehicleModelService.deleteAllByModeloIsNotIn(nomes);
    }

    public List<String[]> readFile(MultipartFile multipartFile) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()));
        String line;

        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("O arquivo enviado está vazio");
        }

        List<String[]> dataArray = new ArrayList<>();
        bufferedReader.readLine();

        while ((line = bufferedReader.readLine()) != null) {
            if(!line.contains(";")){throw new ContentDispositionException("O separdaor do arquivo é invalido, verifique se o arquivo é separado por \";\"");}
            dataArray.add(line.split(";"));
        }

        bufferedReader.close();

        if (dataArray.isEmpty()) {
            throw new ContentDispositionException("O conteudo possuido pelo arquivo não atende o padrão esperado.");
        }

        return dataArray;
    }
}
