package br.com.hbparking.cor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

@Service
public class ColorService {

    public List<Color> getAllColors() {
        return Arrays.asList(Color.values());
    }
}
