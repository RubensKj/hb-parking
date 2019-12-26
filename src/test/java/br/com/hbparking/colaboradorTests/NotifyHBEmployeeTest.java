package br.com.hbparking.colaboradorTests;

import br.com.hbparking.colaborador.NoConnectionAPIException;
import br.com.hbparking.colaborador.NotifyHBEmployee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NotifyHBEmployeeTest {

    @Mock
    private NotifyHBEmployee notifyHBEmployee;

    @Before
    public void SetUp() {
        notifyHBEmployee = new NotifyHBEmployee();
    }

    @Test(expected = NoConnectionAPIException.class)
    public void noConnectioToApiThrowException() throws Exception {
        notifyHBEmployee.notify("teste");
    }

}
