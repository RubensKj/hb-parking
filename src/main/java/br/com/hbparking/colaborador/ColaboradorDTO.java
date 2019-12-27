package br.com.hbparking.colaborador;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class ColaboradorDTO {

    public Long id;
    @Size(max = 150, message = "O email precisa ser informado.")
    @NotNull(message = "O email não pode ser nulo.")
    @NotBlank(message = "O email não pode estar em branco.")
    public String email;
    @Size(max = 35, message = "O nome não pode conter mais de 35 caracteres.")
    @NotBlank(message = "O nome não pode estar em brnaco.")
    @NotNull(message = "o nome precisa ser informado.")
    public String nome;
    @Pattern(regexp = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))", message = "A data de nascimento não corresponde ao padrão esperado.")
    public String dataNascimento;
    public boolean pcd;
    public boolean trabalhoNoturno;
    public boolean resideForaBlumenau;
    public boolean ofereceCarona;

    public ColaboradorDTO() {
    }

    public ColaboradorDTO(String email, String nome, String dataNascimento, boolean pcd, boolean trabalhoNoturno, boolean resideForaBlumenau, boolean ofereceCarona) {
        this.email = email;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.pcd = pcd;
        this.trabalhoNoturno = trabalhoNoturno;
        this.resideForaBlumenau = resideForaBlumenau;
        this.ofereceCarona = ofereceCarona;
    }

    public ColaboradorDTO(Long id, boolean resideForaBlumenau, boolean ofereceCarona) {
        this.id = id;
        this.resideForaBlumenau = resideForaBlumenau;
        this.ofereceCarona = ofereceCarona;
    }

    public static ColaboradorDTO of(Colaborador colaborador){
        ColaboradorDTO colaboradorDTO = new ColaboradorDTO();
        colaboradorDTO.setId(colaborador.getId());
        colaboradorDTO.setDataNascimento(colaborador.getDataNascimento().toString());
        System.out.println(colaborador.getDataNascimento());
        System.out.println(colaboradorDTO.getDataNascimento());
        colaboradorDTO.setEmail(colaborador.getEmail());
        colaboradorDTO.setNome(colaborador.getNome());
        colaboradorDTO.setPcd(colaborador.isPcd());
        colaboradorDTO.setTrabalhoNoturno(colaborador.isTrabalhoNoturno());
        colaboradorDTO.setResideForaBlumenau(colaborador.getResideForaBlumenau());
        colaboradorDTO.setOfereceCarona(colaborador.getOfereceCarona());
        return colaboradorDTO;
    }

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

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
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
