package myapp.presentationmodel.canton;

import myapp.presentationmodel.PMDescription;
import myapp.util.AttributeDescription;
import myapp.util.ValueType;

/**
 * todo: Describe all your application specific PresentationModel-Attributes like this
 */
public enum CantonAtt implements AttributeDescription {
    ID(ValueType.ID),
    NAME(ValueType.STRING),
    CAPITAL(ValueType.STRING);

    private final ValueType valueType;

    CantonAtt(ValueType type) {
        valueType = type;
    }

    @Override
    public ValueType getValueType() {
        return valueType;
    }

    @Override
    public PMDescription getPMDescription() {
        return PMDescription.CANTON;
    }
}
