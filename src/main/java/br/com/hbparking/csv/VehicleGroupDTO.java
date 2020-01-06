package br.com.hbparking.csv;

public class VehicleGroupDTO {

    private Long codigoMarca;
    private String modelo;

    public VehicleGroupDTO() {
    }

    public VehicleGroupDTO(Long codigoMarca, String modelo) {
        this.codigoMarca = codigoMarca;
        this.modelo = modelo;
    }

    public Long getCodigoMarca() {
        return codigoMarca;
    }

    public void setCodigoMarca(Long codigoMarca) {
        this.codigoMarca = codigoMarca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
}
