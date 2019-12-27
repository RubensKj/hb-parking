package br.com.hbparking.colaborador;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "colaboradores")
public class Colaborador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_colaborador")
    private Integer id;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "nome", nullable = false)
    private String nome;
    @Column(name = "data_nascimento", nullable = false)
    private Calendar dataNascimento;
    @Column(name = "pcd", nullable = true)
    private Boolean pcd;
    @Column(name = "trabalho_noturno", nullable = true)
    private Boolean trabalhoNoturno;

    public Colaborador() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Calendar getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Calendar dataNascimento) {
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
}
