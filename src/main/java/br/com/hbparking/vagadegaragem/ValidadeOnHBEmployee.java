package br.com.hbparking.vagadegaragem;

import org.jboss.logging.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
public class ValidadeOnHBEmployee {
    final Logger LOGGER = (Logger) LoggerFactory.getLogger(this.toString());
    public ResponseHBEmployeeDTO validate(String url) throws Exception {
        ResponseHBEmployeeDTO response = new ResponseHBEmployeeDTO();
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.set("Content-type", "application/json");

            HttpEntity<ResponseHBEmployeeDTO> entity = new HttpEntity<>(response, headers);

            ResponseEntity<ResponseHBEmployeeDTO> result = restTemplate.postForEntity(url, entity, ResponseHBEmployeeDTO.class);

        }catch (Exception e){
            //throw new NoConnectionAPIException("Não foi possivel estabelecer uma conexão com HBEmployee");
            LOGGER.info("Vaga cadastrada, porém não foi possivel realizar uma conexão com a pai HbEmployee");
        }
        return  response;
    }
}
