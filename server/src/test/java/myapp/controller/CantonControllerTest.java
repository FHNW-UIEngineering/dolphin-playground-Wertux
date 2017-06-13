package myapp.controller;

import myapp.presentationmodel.PMDescription;
import myapp.presentationmodel.canton.Canton;
import myapp.presentationmodel.canton.CantonAtt;
import myapp.service.SomeService;
import myapp.util.DTOMixin;
import org.junit.Before;
import org.junit.Test;
import org.opendolphin.core.server.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Dieter Holz
 */
public class CantonControllerTest {
    CantonController controller;
    ServerModelStore serverModelStore;
    ServiceStub service;

    @Before
    public void setup() {
        service = new ServiceStub();
        controller = new CantonController(service);

        serverModelStore = new TestModelStore();
        controller.setServerDolphin(new DefaultServerDolphin(serverModelStore, new ServerConnector()));
    }

    @Test
    public void testInitializeBasePMs(){
        //given

        //when
        controller.initializeBasePMs();
        Canton p = controller.getCantonProxy();

        //then
        assertNotNull(p);
        assertFalse(p.isDirty());
    }

    @Test
    public void testDirtyState(){
        //given
        controller.initializeBasePMs();
        Canton p = controller.getCantonProxy();
        String name = p.name.getValue();

        //when
        p.name.setValue(name + " some new value");

        //then
        assertTrue(p.isDirty());
        assertTrue(p.name.isDirty());
    }

    @Test
    public void testLoadPerson(){
        //given
        controller.initializeBasePMs();
        Canton p = controller.getCantonProxy();
        p.name.setValue("bla");

        //when
        ServerPresentationModel pm = controller.loadCanton();

        //then
        assertFalse(p.isDirty());

        //when
        p.name.setValue("xyz");

        //then
        assertEquals("xyz", pm.getAt(CantonAtt.NAME.name()).getValue());

    }

    @Test
    public void testSave(){
        //given
        controller.initializeBasePMs();
        Canton p = controller.getCantonProxy();
        controller.loadCanton();

        p.name.setValue("abc");

        //when
        controller.save();

        //then
        assertEquals(1, service.saveCounter);
        assertFalse(p.isDirty());
    }

    private class ServiceStub implements SomeService, DTOMixin {
        int saveCounter;

        @Override
        public DTO loadSomeEntity() {
            return createDTO(PMDescription.CANTON);
        }

        @Override
        public void save(List<DTO> dtos) {
            saveCounter++;
        }
    }

    private class TestModelStore extends ServerModelStore{
        TestModelStore(){
            setCurrentResponse(new ArrayList<>());
        }
    }



}