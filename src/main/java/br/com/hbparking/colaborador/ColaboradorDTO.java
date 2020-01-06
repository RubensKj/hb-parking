package br.com.hbparking.colaborador;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class ColaboradorDTO {
    private Long id;
    @Size(max = 150, message = "O email precisa ser informado.")
    @NotNull(message = "O email não pode ser nulo.")
    @NotBlank(message = "O email não pode estar em branco.")
    private String email;
    @Size(max = 35, message = "O nome não pode conter mais de 35 caracteres.")
    @NotBlank(message = "O nome não pode estar em brnaco.")
    @NotNull(message = "o nome precisa ser informado.")
    private String nome;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;
    private boolean pcd;
    private boolean trabalhoNoturno;
    private boolean resideForaBlumenau;
    private boolean ofereceCarona;

    public ColaboradorDTO() {
    }

    public ColaboradorDTO(String email, String nome, LocalDate dataNascimento, boolean pcd, boolean trabalhoNoturno, boolean resideForaBlumenau, boolean ofereceCarona) {
        this.email = email;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.pcd = pcd;
        this.trabalhoNoturno = trabalhoNoturno;
        this.resideForaBlumenau = resideForaBlumenau;
        this.ofereceCarona = ofereceCarona;
    }

    public ColaboradorDTO(@Size(max = 150, message = "O email precisa ser informado.") @NotNull(message = "O email não pode ser nulo.") @NotBlank(message = "O email não pode estar em branco.") String email, @Size(max = 35, message = "O nome não pode conter mais de 35 caracteres.") @NotBlank(message = "O nome não pode estar em brnaco.") @NotNull(message = "o nome precisa ser informado.") String nome, LocalDate dataNascimento, boolean pcd, boolean trabalhoNoturno) {
        this.email = email;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.pcd = pcd;
        this.trabalhoNoturno = trabalhoNoturno;
    }

    public ColaboradorDTO(Long id, boolean resideForaBlumenau, boolean ofereceCarona) {
        this.id = id;
        this.resideForaBlumenau = resideForaBlumenau;
        this.ofereceCarona = ofereceCarona;
    }

    public static ColaboradorDTO of(Colaborador colaborador) {
        ColaboradorDTO colaboradorDTO = new ColaboradorDTO();
        colaboradorDTO.setId(colaborador.getId());
        colaboradorDTO.setDataNascimento(colaborador.getDataNascimento());
        colaboradorDTO.setEmail(colaborador.getEmail());
        colaboradorDTO.setNome(colaborador.getNome());
        colaboradorDTO.setPcd(colaborador.isPcd());
        colaboradorDTO.setTrabalhoNoturno(colaborador.isTrabalhoNoturno());
        colaboradorDTO.setResideForaBlumenau(colaborador.isResideForaBlumenau());
        colaboradorDTO.setOfereceCarona(colaborador.isOfereceCarona());

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
