package br.com.hbparking.csv;

import br.com.hbparking.vehicleException.ContentDispositionException;
import br.com.hbparking.vehicleModel.VehicleModel;
import br.com.hbparking.vehicleModel.VehicleModelDTO;
import br.com.hbparking.vehicleModel.VehicleModelService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class VehicleModelImport {
    private final VehicleModelService vehicleModelService;

    public VehicleModelImport(VehicleModelService vehicleModelService) {
        this.vehicleModelService = vehicleModelService;
    }

    public void importVehicle(MultipartFile multipartFile, HttpServletResponse response) throws Exception {
        List<String[]> dataArray = this.readFile(multipartFile);
        List<VehicleModel> vehicleModelList = new ArrayList<>();

        //clear the table
        this.vehicleModelService.clearTable();

        dataArray.forEach(lineData -> {
            for (int i = 0; i < lineData.length; i += 3) {
                VehicleModelDTO vehicleModelDTO = new VehicleModelDTO();
                if (!(lineData[i].isEmpty()) && !(lineData[i + 1].isEmpty())) {

                    VehicleModel vehicleModel = new VehicleModel();

                    vehicleModel.setModelo(lineData[i + 1]);
                    vehicleModel.setIdMarca(new Long(lineData[i]));

                    vehicleModelList.add(vehicleModel);
                }
            }
        });

        this.vehicleModelService.saveVehicle(vehicleModelList);
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
            dataArray.add(line.split(";"));
        }

        bufferedReader.close();

        if (dataArray.isEmpty()) {
            throw new ContentDispositionException("O conteudo possuido pelo arquivo não atende o padrão esperado.");
        }

        return dataArray;
    }
}
