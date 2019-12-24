package br.com.hbparking.termoLocacao;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "rental_term")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RentalTerm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @Column(name = "title", length = 150, nullable = false, updatable = false)
    @Getter @Setter
    private String title;

    @Column(name = "file_name", nullable = false, updatable = false)
    @Getter @Setter
    private String fileName;

    @Column(name = "status_rental_term", nullable = false)
    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private RentalTermStatus rentalTermStatus;

    public RentalTerm(String title, String fileName, RentalTermStatus status) {
        this.title = title;
        this.fileName = fileName;
        this.rentalTermStatus = status;
    }


}
