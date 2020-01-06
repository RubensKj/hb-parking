package br.com.hbparking.email;

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


    public void sendEmails(List<VagaGaragem> vagasSorteadas){

        try{

        for(int i = 0; i < vagasSorteadas.size(); i++){


            message.setTo(vagasSorteadas.get(i).getColaborador().getEmail());
            message.setSubject("Atualização do status da vaga");
            message.setText("Parabéns colaborador, sua vaga foi aprovada!");

            javaMailSender.send(message);
        }

        }catch (Exception e){
            LOGGER.error("Deu pau: {}", e.getMessage());
        }
    }


}
