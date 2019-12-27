package br.com.hbparking.vagadegaragem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioTemporario {
    private Long id;
    private boolean moraFora;
    private boolean ofereceCarona;
}
