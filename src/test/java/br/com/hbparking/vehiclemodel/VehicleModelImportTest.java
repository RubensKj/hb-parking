package br.com.hbparking.vehiclemodel;

import br.com.hbparking.csv.VehicleModelImport;
import br.com.hbparking.marcas.IMarcaRepository;
import br.com.hbparking.marcas.MarcaService;
import br.com.hbparking.util.ReadFileCSV;
import br.com.hbparking.vehicleexception.ContentDispositionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

@RunWith(MockitoJUnitRunner.class)
public class VehicleModelImportTest {

    @Mock
    private IVehicleModelRepository iVehicleModelRepository;
    @Mock
    private IMarcaRepository marcaRepository;
    @InjectMocks
    private VehicleModelService vehicleModelService;
    @InjectMocks
    private VehicleModelImport vehicleModelImport;
    @InjectMocks
    private MarcaService marcaService;
    @Mock
    private ReadFileCSV readFile;

    //testing that case the file uploaded is empty will return IllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void fileEmptyReturnException() throws Exception {

        MockMultipartFile mockMultipartFile = new MockMultipartFile("user-file", "fileName",
                "text/plain", new byte[0]);
        this.readFile.read(mockMultipartFile);
    }

    //testing that case the content doesn't make sense to the program it will return a ContentDispositionException
    @Test(expected = ContentDispositionException.class)
    public void fileContentDispositionNotExpected() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("user-file", "fileName",
                "text/plain", "teste".getBytes());
        this.readFile.read(mockMultipartFile);
    }

    //file doesn't contains ";", should return ContentDispositionException
    @Test(expected = ContentDispositionException.class)
    public void fileSeparatorInvalid() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("user-file", "fileName",
                "text/plain", "header;header\n2 teste".getBytes());
        this.readFile.read(mockMultipartFile);
    }
}
