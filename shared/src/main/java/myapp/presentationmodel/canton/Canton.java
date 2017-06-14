package myapp.presentationmodel.canton;

import myapp.util.veneer.LongAttributeFX;
import myapp.util.veneer.PresentationModelVeneer;
import myapp.util.veneer.StringAttributeFX;
import org.opendolphin.core.BasePresentationModel;

/**
 * @author Dieter Holz
 */
public class Canton extends PresentationModelVeneer {
    public Canton(BasePresentationModel pm) {
        super(pm);
    }

    public final LongAttributeFX    id      = new LongAttributeFX(getPresentationModel()   , CantonAtt.ID);
    public final StringAttributeFX  name    = new StringAttributeFX(getPresentationModel() , CantonAtt.NAME);
    public final StringAttributeFX capital = new StringAttributeFX(getPresentationModel(), CantonAtt.CAPITAL);
}
