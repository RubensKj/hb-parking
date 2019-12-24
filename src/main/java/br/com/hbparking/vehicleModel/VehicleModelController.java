package br.com.hbparking.vehicleModel;

import br.com.hbparking.csvVehicleModel.VehicleModelExport;
import br.com.hbparking.csvVehicleModel.VehicleModelImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/model")
@CrossOrigin(origins ="*", maxAge = 36000)
public class VehicleModelController {

    private final VehicleModelExport vehicleModelExport;
    private final VehicleModelImport vehicleModelImport;

    @Autowired
    public VehicleModelController(VehicleModelExport vehicleModelExport, VehicleModelImport vehicleModelImport) {
        this.vehicleModelExport = vehicleModelExport;
        this.vehicleModelImport = vehicleModelImport;
    }

    //Busca Export
    @GetMapping("/export/models")
    public void exportar(HttpServletResponse response) throws Exception {
        this.vehicleModelExport.export(response);
    }

    //import vehicle model
    @PostMapping("/import/models")
    public void vehicleImport(@RequestParam("file")MultipartFile multipartFile, HttpServletResponse response) throws Exception {
        vehicleModelImport.importVehicle(multipartFile, response);
    }


}
