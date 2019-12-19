package br.com.hbparking.cor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebMvcTest(ColorController.class)
public class ColorControllerTest {

    @Autowired
    private ColorController colorController;

    @MockBean
    private ColorService colorService;

    @Test
    public void getColours_ShouldReturnAllCars() {
        given(colorService.getAllColors()).willReturn(new ArrayList<>(EnumSet.allOf(Color.class)));

        List<Color> colorList = colorController.getAllColors();

        assertThat(colorList).isNotNull();
        assertThat(colorList).isNotEmpty();
        assertThat(colorList).isEqualTo(Arrays.asList(Color.values()));
    }
}
