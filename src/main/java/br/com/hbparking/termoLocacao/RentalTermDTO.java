package br.com.hbparking.termoLocacao;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalTermDTO {

    private Long id;
    private String title;
    private String fileName;
    private String rentalTermStatus;

    public RentalTermDTO(String title, String fileName, String rentalTermStatus) {
        this.title = title;
        this.fileName = fileName;
        this.rentalTermStatus = rentalTermStatus;
    }

    public static RentalTermDTO of(RentalTerm rentalTerm) {
        return new RentalTermDTO(rentalTerm.getId(),
                rentalTerm.getTitle(),
                rentalTerm.getFileName(),
                rentalTerm.getRentalTermStatus().name());
    }


}
