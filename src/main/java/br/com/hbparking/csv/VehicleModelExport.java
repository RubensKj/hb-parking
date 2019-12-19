package br.com.hbparking.csv;

import br.com.hbparking.vehicleModel.VehicleModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.*;

@Service
public class VehicleModelExport {

    private static final String FILE_NAME = "Modelo de veiculos";

    private final VehicleModelService vehicleModelService;

    @Autowired
    public VehicleModelExport(VehicleModelService vehicleModelService) {
        this.vehicleModelService = vehicleModelService;
    }

    public void export(HttpServletResponse response) throws Exception {
        //setting the response headers
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + this.generateFileName() + ".csv");

        //map containing a list of String, that is the name of the vehicle model, ordered by brand

        Map<Long, List<String>> mapOfModelsByBrand = this.vehicleModelService.getVehicleModelsOrderedByBrand();

        Set<Long> brandCodeList = mapOfModelsByBrand.keySet();
        int maxQtdOfLines = this.getQuantityOfLines(mapOfModelsByBrand);
        String csvText = this.getDefaultHeaders(brandCodeList);

        for (int i = 0; i < maxQtdOfLines; i++) {
            int j = 0;
            for (Long codigoMarca : brandCodeList) {
                j++;
                List<String> vehicleModelList = mapOfModelsByBrand.get(codigoMarca);
                if (i < vehicleModelList.size()) {
                    csvText = csvText.concat(this.addInfoToLine(codigoMarca.toString(), vehicleModelList.get(i), j));
                } else {
                    csvText = csvText.concat(this.contentNotExistsAddTwoColumns(j));
                }
            }
            csvText = csvText.concat("\n");
        }

        PrintWriter writer = response.getWriter();
        writer.print(csvText);
        writer.close();
    }

    private String generateFileName() {
        String fileName = FILE_NAME.trim().replaceAll(" ", "_");
        fileName = fileName.concat("_" + new Date().getTime());
        return fileName;
    }

    private int getQuantityOfLines(Map<Long, List<String>> map) {
        return map
                .values()
                .stream()
                .mapToInt(List::size)
                .max()
                .orElse(0);
    }

    private String getDefaultHeaders(Set<Long> idBrands) {
        String csvInteiro = "";
        for (Long codigoMarca : idBrands) {
            csvInteiro += "Codigo Marca;Descrição;;";
        }
        csvInteiro += "\n";

        return csvInteiro;
    }

    private String addInfoToLine(String brandCode, String vehicleModel, int lineNumber) {
        String line = "";
        line = line.concat(this.isEmptyLineAddSpace(lineNumber));
        line = line.concat(brandCode + ";" + vehicleModel + ";");
        return line;
    }

    private String contentNotExistsAddTwoColumns(int lineNumber) {
        String line = "";
        line = line.concat(this.isEmptyLineAddSpace(lineNumber));
        line = line.concat(";;");
        return line;
    }

    private String isEmptyLineAddSpace(int line) {
        String lineCsv = "";
        if (line > 1) {
            return lineCsv.concat(";");
        }
        return lineCsv;
    }
}
