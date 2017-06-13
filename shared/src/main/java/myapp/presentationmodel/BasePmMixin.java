package myapp.presentationmodel;

import org.opendolphin.core.BasePresentationModel;
import org.opendolphin.core.Dolphin;

import myapp.presentationmodel.canton.Canton;
import myapp.presentationmodel.presentationstate.ApplicationState;

/**
 * @author Dieter Holz
 */
public interface BasePmMixin {
    //todo: for all your basePMs (as delivered by your Controllers) specify constants and getter-methods like these
    String CANTON_PROXY_PM_ID = PMDescription.CANTON.pmId(-777L);

    default BasePresentationModel getCantonProxyPM() {
        return (BasePresentationModel) getDolphin().getAt(CANTON_PROXY_PM_ID);
    }

    default Canton getCantonProxy() {
        return new Canton(getCantonProxyPM());
    }

    // always needed
    String APPLICATION_STATE_PM_ID = PMDescription.APPLICATION_STATE.pmId(-888);

    default BasePresentationModel getApplicationStatePM() {
        return (BasePresentationModel) getDolphin().getAt(APPLICATION_STATE_PM_ID);
    }

    default ApplicationState getApplicationState() {
        return new ApplicationState(getApplicationStatePM());
    }

    Dolphin getDolphin();
}
