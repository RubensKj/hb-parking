package br.com.hbparking.vehicleModel;

import br.com.hbparking.csv.VehicleModelExport;
import br.com.hbparking.csv.VehicleModelImport;
import br.com.hbparking.marcas.CannotFindAnyMarcaWithId;
import br.com.hbparking.marcas.Marca;
import br.com.hbparking.marcas.MarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/model")
public class VehicleModelController {

    private final VehicleModelExport vehicleModelExport;
    private final VehicleModelImport vehicleModelImport;
    private final MarcaService marcaService;
    private final VehicleModelService vehicleModelService;

    @Autowired
    public VehicleModelController(VehicleModelExport vehicleModelExport, VehicleModelImport vehicleModelImport, MarcaService marcaService, VehicleModelService vehicleModelService) {
        this.vehicleModelExport = vehicleModelExport;
        this.vehicleModelImport = vehicleModelImport;
        this.marcaService = marcaService;
        this.vehicleModelService = vehicleModelService;
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

    @GetMapping("/findModel/{idMarca}/{modelo}")
    public List<VehicleModel> findModel(@PathVariable("idMarca") Long idMarca, @PathVariable("modelo") String modelo) throws CannotFindAnyMarcaWithId {
        Marca marca = this.marcaService.findEntityById(idMarca);
        return vehicleModelService.findByMarcaAndModelo(marca, modelo);
    }





}
