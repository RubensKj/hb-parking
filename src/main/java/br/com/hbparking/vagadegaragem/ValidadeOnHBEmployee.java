package br.com.hbparking.vagadegaragem;

import br.com.hbparking.colaborador.NoConnectionAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
public class ValidadeOnHBEmployee {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidadeOnHBEmployee.class);

    public ResponseHBEmployeeDTO validate(String url) throws NoConnectionAPIException {
        ResponseHBEmployeeDTO response = new ResponseHBEmployeeDTO();
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.set("Content-type", "application/json");

            HttpEntity<ResponseHBEmployeeDTO> entity = new HttpEntity<>(response, headers);

            ResponseEntity<ResponseHBEmployeeDTO> result = restTemplate.postForEntity(url, entity, ResponseHBEmployeeDTO.class);
            response = result.getBody();

        } catch (Exception e) {
            LOGGER.info("Vaga cadastrada, porém não foi possivel realizar uma conexão com a pai HbEmployee");
            //throw new NoConnectionAPIException("Não foi possivel estabelecer uma conexão com HBEmployee");

        }

        return response;
    }
}
