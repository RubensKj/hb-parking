package br.com.hbparking.termoLocacao;

import org.springframework.core.io.Resource;

public class RentalTermDTO {

    private Long id;
    private String title;
    private String fileName;
    private String rentalTermStatus;

    public RentalTermDTO() {
    }

    public RentalTermDTO(Long id, String title, String fileName, String rentalTermStatus) {
        this.id = id;
        this.title = title;
        this.fileName = fileName;
        this.rentalTermStatus = rentalTermStatus;
    }

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

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getFileName() {
        return fileName;
    }

    public String getRentalTermStatus() {
        return rentalTermStatus;
    }
}
