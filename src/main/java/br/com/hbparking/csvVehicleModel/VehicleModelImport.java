package br.com.hbparking.csvVehicleModel;

import br.com.hbparking.marcas.CannotFindAnyMarcaWithId;
import br.com.hbparking.marcas.Marca;
import br.com.hbparking.marcas.MarcaService;
import br.com.hbparking.util.ReadFileCSV;
import br.com.hbparking.vehicleModel.VehicleModel;
import br.com.hbparking.vehicleModel.VehicleModelService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class VehicleModelImport {
    private final VehicleModelService vehicleModelService;
    private final MarcaService marcaService;
    private final ReadFileCSV readFile;

    public VehicleModelImport(VehicleModelService vehicleModelService, MarcaService marcaService, ReadFileCSV readFile) {
        this.vehicleModelService = vehicleModelService;
        this.marcaService = marcaService;
        this.readFile = readFile;
    }

    @Transactional
    public void importVehicle(MultipartFile multipartFile, HttpServletResponse response) throws Exception {
        List<String[]> dataArray = this.readFile.read(multipartFile);
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

}
