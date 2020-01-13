package br.com.hbparking.colaborador;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "colaboradores")
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_colaborador")
    private Long id;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "nome", nullable = false)
    private String nome;
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;
    @Column(name = "pcd", nullable = true)
    private Boolean pcd;
    @Column(name = "trabalho_noturno", nullable = true)
    private Boolean trabalhoNoturno;
    @Column(name = "reside_fora_blumenau", nullable = false)
    private boolean resideForaBlumenau;
    @Column(name = "oferece_carona", nullable = false)
    private boolean ofereceCarona;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public boolean isPcd() {
        return pcd;
    }

    public void setPcd(boolean pcd) {
        this.pcd = pcd;
    }

    public boolean isTrabalhoNoturno() {
        return trabalhoNoturno;
    }

    public void setTrabalhoNoturno(boolean trabalhoNoturno) {
        this.trabalhoNoturno = trabalhoNoturno;
    }

    public boolean isResideForaBlumenau() {
        return resideForaBlumenau;
    }

    public void setResideForaBlumenau(boolean resideForaBlumenau) {
        this.resideForaBlumenau = resideForaBlumenau;
    }

    public boolean isOfereceCarona() {
        return ofereceCarona;
    }

    public void setOfereceCarona(boolean ofereceCarona) {
        this.ofereceCarona = ofereceCarona;
    }
}
