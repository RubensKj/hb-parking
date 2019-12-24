package br.com.hbparking.termoLocacao;

import lombok.*;

@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RentalTermDTO {

    @Getter @Setter
    private Long id;
    @Getter @Setter
    private String title;
    @Getter @Setter
    private String fileName;
    @Getter @Setter
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
