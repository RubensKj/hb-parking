package br.com.hbparking.vehicleModel;

public class VehicleModelDTO {

    private Long id;
    private Long idMarca;
    private String modelo;

    public VehicleModelDTO() {
    }

    public VehicleModelDTO(Long idMarca, String modelo) {
        this.idMarca = idMarca;
        this.modelo = modelo;
    }

    private VehicleModelDTO(Long id, Long idMarca, String modelo) {
        this.id = id;
        this.idMarca = idMarca;
        this.modelo = modelo;
    }

    public static VehicleModelDTO of(VehicleModel vehicleModel){
        return new VehicleModelDTO(
                vehicleModel.getId(),
                vehicleModel.getFkMarca().getId(),
                vehicleModel.getModelo()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(Long idMarca) {
        this.idMarca = idMarca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
}
