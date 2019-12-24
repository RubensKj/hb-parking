package br.com.hbparking.termoLocacao;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "rental_term")
@NoArgsConstructor
@AllArgsConstructor
@Data
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


}
