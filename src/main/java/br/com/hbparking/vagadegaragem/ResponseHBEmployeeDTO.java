package br.com.hbparking.vagadegaragem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseHBEmployeeDTO {

    private String employeeName;
    private Boolean parkingValid = true;
    private String justification;

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Boolean getParkingValid() {
        return parkingValid;
    }

    public void setParkingValid(Boolean parkingValid) {
        this.parkingValid = parkingValid;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }
}
