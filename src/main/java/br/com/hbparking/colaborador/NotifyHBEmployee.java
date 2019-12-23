package br.com.hbparking.colaborador;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
public class NotifyHBEmployee {
    public void notify(String url) throws Exception {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.set("Content-type", "application/json");

            HttpEntity<String> entity = new HttpEntity<String>("teste", headers);

            ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        }catch (Exception e){
            throw new NoConnectionAPI("Colaborador cadastrado, proém não foi possivel realizar uma conexão com a pai HbEmployee.");
        }
    }
}
