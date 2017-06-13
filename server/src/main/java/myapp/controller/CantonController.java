package myapp.controller;

import myapp.presentationmodel.BasePmMixin;
import myapp.presentationmodel.PMDescription;
import myapp.presentationmodel.canton.Canton;
import myapp.presentationmodel.canton.CantonCommands;
import myapp.service.SomeService;
import myapp.util.Controller;
import org.opendolphin.core.Dolphin;
import org.opendolphin.core.server.DTO;
import org.opendolphin.core.server.ServerPresentationModel;
import org.opendolphin.core.server.comm.ActionRegistry;

import java.util.List;

/**
 * This is an example for an application specific controller.
 * <p>
 * Controllers may have many actions that serve a common purpose.
 * <p>
 * Todo: Replace this with your Controller
 */
class CantonController extends Controller implements BasePmMixin {

    private final SomeService service;

    private Canton cantonProxy;

    CantonController(SomeService service) {
        this.service = service;
    }

    @Override
    public void registerCommands(ActionRegistry registry) {
        registry.register(CantonCommands.LOAD_SOME_CANTON, ($, $$) -> loadCanton());
        registry.register(CantonCommands.SAVE            , ($, $$) -> save());
        registry.register(CantonCommands.RESET           , ($, $$) -> reset(PMDescription.CANTON));
    }

    @Override
    protected void initializeBasePMs() {
        ServerPresentationModel pm = createProxyPM(PMDescription.CANTON, CANTON_PROXY_PM_ID);

        cantonProxy = new Canton(pm);
    }

    @Override
    protected void setDefaultValues() {
        cantonProxy.name.setMandatory(true);
    }

    @Override
    protected void setupValueChangedListener() {
        getApplicationState().language.valueProperty().addListener((observable, oldValue, newValue) -> translate(cantonProxy, newValue));
    }

    ServerPresentationModel loadCanton() {
        DTO dto = service.loadSomeEntity();
        ServerPresentationModel pm = createPM(PMDescription.CANTON, dto);

        cantonProxy.getPresentationModel().syncWith(pm);

        return pm;
    }

    void save() {
        List<DTO> dtos = dirtyDTOs(PMDescription.CANTON);
        service.save(dtos);
        rebase(PMDescription.CANTON);
    }

    @Override
    public Dolphin getDolphin() {
        return getServerDolphin();
    }
}
