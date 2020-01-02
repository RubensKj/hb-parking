package br.com.hbparking.termoLocacao;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "rental_term")
public class RentalTerm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 150, nullable = false, updatable = false)
    private String title;

    @Column(name = "file_name", nullable = false, updatable = false)
    private String fileName;

    @Column(name = "status_rental_term", nullable = false)
    @Enumerated(EnumType.STRING)
    private RentalTermStatus rentalTermStatus;

    public RentalTerm(String title, String fileName, RentalTermStatus status) {
        this.title = title;
        this.fileName = fileName;
        this.rentalTermStatus = status;
    }

    public RentalTerm() {
    }

    public RentalTerm(Long id, String title, String fileName, RentalTermStatus rentalTermStatus) {
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

    public RentalTermStatus getRentalTermStatus() {
        return rentalTermStatus;
    }

    public void setRentalTermStatus(RentalTermStatus rentalTermStatus) {
        this.rentalTermStatus = rentalTermStatus;
    }
}
