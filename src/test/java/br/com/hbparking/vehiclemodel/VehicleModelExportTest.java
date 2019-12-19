package br.com.hbparking.vehiclemodel;

import br.com.hbparking.vehicleException.EmptyMapException;
import br.com.hbparking.vehicleModel.IVehicleModelRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VehicleModelExportTest {
    @Mock
    private IVehicleModelRepository vehicleModelRepository;

    @Test(expected = EmptyMapException.class)
    public void emptyMapReturnException() throws Exception{

    }
}
