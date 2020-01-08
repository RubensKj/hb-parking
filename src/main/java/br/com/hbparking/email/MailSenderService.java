package br.com.hbparking.email;

import br.com.hbparking.util.DateHelper;
import br.com.hbparking.vagadegaragem.VagaGaragem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailSenderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailSenderService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    private SimpleMailMessage message = new SimpleMailMessage();

    public void successListToBeEmailed(List<VagaGaragem> vagasSorteadas) {

        try{
            vagasSorteadas.forEach(this::sendEmailSuccess);
        } catch (Exception e) {
            LOGGER.error("Erro: {}", e.getMessage());
        }
    }

    public void sendEmailSuccess(VagaGaragem vagaGaragem) {

        String dataInicial = DateHelper.formatDate(vagaGaragem.getPeriodo().getDataInicial());
        String dataFinal = DateHelper.formatDate(vagaGaragem.getPeriodo().getDataFinal());

        message.setTo(vagaGaragem.getColaborador().getEmail());
        message.setSubject("Atualização do status da vaga");
        message.setText("Olá, " + vagaGaragem.getColaborador().getNome() +
                ". Sua solicitação de vaga para o " + vagaGaragem.getVehicleModel().getModelo() + " de placa " + vagaGaragem.getPlaca() +
                " foi aprovada para o período de " + dataInicial + " até " + dataFinal +
                ".\nFaça bom proveito! \n\nWe're here for tech and beer!");

        javaMailSender.send(message);
    }

    public void sendEmailDisapproved(VagaGaragem vagaGaragem) {
        message.setTo(vagaGaragem.getColaborador().getEmail());
        message.setSubject("Atualização do status da vaga");
        message.setText("Olá, " + vagaGaragem.getColaborador().getNome() +
                        ". Sua solicitação de vaga para o " + vagaGaragem.getVehicleModel().getModelo() + " de placa " + vagaGaragem.getPlaca() +
                        " foi reprovada.");

        javaMailSender.send(message);
    }

    public void disapprovedListToBeEmailed(List<VagaGaragem> vagasSorteadas) {

        try {
            vagasSorteadas.forEach(this::sendEmailDisapproved);
        }catch (Exception e){
            LOGGER.error("Erro: {}", e.getMessage());
        }
    }


}
