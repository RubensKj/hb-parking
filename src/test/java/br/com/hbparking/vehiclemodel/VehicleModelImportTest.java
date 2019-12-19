package br.com.hbparking.vehiclemodel;

import br.com.hbparking.vehicleException.ContentDispositionException;
import br.com.hbparking.vehicleModel.IVehicleModelRepository;
import br.com.hbparking.csv.VehicleModelImport;
import br.com.hbparking.vehicleModel.VehicleModelService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

@RunWith(MockitoJUnitRunner.class)
public class VehicleModelImportTest {

    @Mock
    private IVehicleModelRepository iVehicleModelRepository;

    private VehicleModelService vehicleModelService;

    private VehicleModelImport vehicleModelImport;

    @Before
    public void setUp() {
        vehicleModelService = new VehicleModelService(iVehicleModelRepository);
        vehicleModelImport = new VehicleModelImport(vehicleModelService);
    }

    //testing that case the file uploaded is empty will return IllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void fileEmptyReturnException() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("user-file", "fileName",
                "text/plain", new byte[0]);
        vehicleModelImport.readFile(mockMultipartFile);
    }

    //testing that case the content doesn't make sense to the program it will return a ContentDispositionException
    @Test(expected = ContentDispositionException.class)
    public void fileContentDispositionNotExpected() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("user-file", "fileName",
                "text/plain", "teste".getBytes());
        vehicleModelImport.readFile(mockMultipartFile);
    }

}
