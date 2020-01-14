package br.com.hbparking.csv;

import br.com.hbparking.vehiclemodel.VehicleModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class VehicleModelExport {

    private static final String FILE_NAME = "Modelo de veiculos";

    private final VehicleModelService vehicleModelService;

    @Autowired
    public VehicleModelExport(VehicleModelService vehicleModelService) {
        this.vehicleModelService = vehicleModelService;
    }

    public void export(HttpServletResponse response) throws Exception {
        //setting the response headers
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + this.generateFileName() + "\"" + ".csv");

        Map<Long, List<VehicleGroupDTO>> mapOfModelsOrderedByBrand = this.vehicleModelService.getVehicleModelsOrderedByBrand();

        if (mapOfModelsOrderedByBrand.isEmpty()){
            throw new NoResultException("Não foi encontrado nenhum registro.");
        }

        Set<Long> brandCodeList = mapOfModelsOrderedByBrand.keySet();
        int maxQtdOfLines = this.getQuantityOfLines(mapOfModelsOrderedByBrand);
        String csvText = this.getDefaultHeaders(brandCodeList);

        for (int i = 0; i < maxQtdOfLines; i++) {
            int j = 0;
            for (Long brand : brandCodeList) {
                j++;
                List<VehicleGroupDTO> vehicleModelList = mapOfModelsOrderedByBrand.get(brand);
                if (i < vehicleModelList.size()) {
                    csvText = csvText.concat(this.addInfoToLine(brand.toString(), vehicleModelList.get(i).getModelo(), j));
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
        String fileName = FILE_NAME.trim().replace(" ", "_");
        fileName = fileName.concat("_" + new Date().getTime());
        return fileName;
    }

    private int getQuantityOfLines(Map<Long, List<VehicleGroupDTO>> map) {
        return map
                .values()
                .stream()
                .mapToInt(List::size)
                .max()
                .orElse(0);
    }

    private String getDefaultHeaders(Set<Long> idBrands) {
        String csvInteiro = "";
        for (Long brand : idBrands) {
            csvInteiro = csvInteiro.concat("Codigo Marca;Descrição;;");
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
