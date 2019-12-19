package br.com.hbparking.cor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ColorServiceTest {

    private ColorService colorService;

    @Before
    public void setUp() {
        colorService = new ColorService();
    }

    @Test
    public void getAllColorsTest() {

        List<Color> colorList = colorService.getAllColors();

        assertThat(colorList).isNotNull();
        assertThat(colorList).isNotEmpty();
        assertThat(colorList).isEqualTo(Arrays.asList(Color.values()));
    }
}
