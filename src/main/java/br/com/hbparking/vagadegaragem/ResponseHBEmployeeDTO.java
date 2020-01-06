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
}
