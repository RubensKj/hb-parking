package br.com.hbparking.termoLocacao;

import lombok.*;

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

    public RentalTermDTO() {
    }

    public RentalTermDTO(Long id, String title, String fileName, String rentalTermStatus) {
        this.id = id;
        this.title = title;
        this.fileName = fileName;
        this.rentalTermStatus = rentalTermStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getRentalTermStatus() {
        return rentalTermStatus;
    }

    public void setRentalTermStatus(String rentalTermStatus) {
        this.rentalTermStatus = rentalTermStatus;
    }
}