package myapp;

import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import org.opendolphin.binding.Converter;
import org.opendolphin.binding.JFXBinder;
import org.opendolphin.core.Attribute;
import org.opendolphin.core.Dolphin;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.Tag;
import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientPresentationModel;

import myapp.presentationmodel.BasePmMixin;
import myapp.presentationmodel.canton.Canton;
import myapp.presentationmodel.canton.CantonAtt;
import myapp.presentationmodel.canton.CantonCommands;
import myapp.presentationmodel.presentationstate.ApplicationState;
import myapp.presentationmodel.presentationstate.ApplicationStateAtt;
import myapp.util.AdditionalTag;
import myapp.util.Language;
import myapp.util.ViewMixin;
import myapp.util.veneer.AttributeFX;
import myapp.util.veneer.BooleanAttributeFX;

/**
 * Implementation of the view details, event handling, and binding.
 *
 * @author Dieter Holz
 *
 * todo : Replace it with your application UI
 */
class RootPane extends GridPane implements ViewMixin, BasePmMixin {

    private static final String DIRTY_STYLE     = "dirty";
    private static final String INVALID_STYLE   = "invalid";
    private static final String MANDATORY_STYLE = "mandatory";

    // clientDolphin is the single entry point to the PresentationModel-Layer
    private final ClientDolphin clientDolphin;

    private Label headerLabel;

    private Label idLabel;
    private Label idField;

    private Label     nameLabel;
    private TextField nameField;

    private Label capitalLabel;
    private TextField capitalField;


    private Button saveButton;
    private Button resetButton;
    private Button nextButton;
    private Button germanButton;
    private Button englishButton;

    private final Canton cantonProxy;

    //always needed
    private final ApplicationState ps;

    RootPane(ClientDolphin clientDolphin) {
        this.clientDolphin = clientDolphin;
        ps = getApplicationState();
        cantonProxy = getCantonProxy();

        init();
    }

    @Override
    public Dolphin getDolphin() {
        return clientDolphin;
    }

    @Override
    public void initializeSelf() {
        addStylesheetFiles("/fonts/fonts.css", "/myapp/myApp.css");
        getStyleClass().add("rootPane");
    }

    @Override
    public void initializeParts() {
        headerLabel = new Label();
        headerLabel.getStyleClass().add("heading");

        idLabel = new Label();
        idField = new Label();

        nameLabel = new Label();
        nameField = new TextField();

        capitalLabel = new Label();
        capitalField = new TextField();


        saveButton    = new Button("Save");
        resetButton   = new Button("Reset");
        nextButton    = new Button("Next");
        germanButton  = new Button("German");
        englishButton = new Button("English");
    }

    @Override
    public void layoutParts() {
        ColumnConstraints grow = new ColumnConstraints();
        grow.setHgrow(Priority.ALWAYS);

        getColumnConstraints().setAll(new ColumnConstraints(), grow);
        setVgrow(headerLabel, Priority.ALWAYS);

        add(headerLabel    , 0, 0, 5, 1);
        add(idLabel        , 0, 1);
        add(idField        , 1, 1, 4, 1);
        add(nameLabel      , 0, 2);
        add(nameField      , 1, 2, 4, 1);
        add(capitalLabel, 0, 3);
        add(capitalField, 1, 3, 4, 1);
        add(new HBox(5, saveButton, resetButton, nextButton, germanButton, englishButton), 0, 4, 5, 1);
    }

    @Override
    public void setupEventHandlers() {
        // all events either send a command (needs to be registered in a controller on the server side)
        // or set a value on an Attribute

        ApplicationState ps = getApplicationState();
        saveButton.setOnAction(   $ -> clientDolphin.send(CantonCommands.SAVE));
        resetButton.setOnAction(  $ -> clientDolphin.send(CantonCommands.RESET));
        nextButton.setOnAction(   $ -> clientDolphin.send(CantonCommands.LOAD_SOME_CANTON));

        germanButton.setOnAction( $ -> ps.language.setValue(Language.GERMAN));
        englishButton.setOnAction($ -> ps.language.setValue(Language.ENGLISH));
    }

    @Override
    public void setupValueChangedListeners() {
        cantonProxy.name.dirtyProperty().addListener((observable, oldValue, newValue)    -> updateStyle(nameField      , DIRTY_STYLE, newValue));
        cantonProxy.capital.dirtyProperty().addListener((observable, oldValue, newValue)     -> updateStyle(capitalField, DIRTY_STYLE, newValue));

        cantonProxy.name.validProperty().addListener((observable, oldValue, newValue)    -> updateStyle(nameField      , INVALID_STYLE, !newValue));
        cantonProxy.capital.validProperty().addListener((observable, oldValue, newValue)     -> updateStyle(capitalField, INVALID_STYLE, !newValue));

        cantonProxy.name.mandatoryProperty().addListener((observable, oldValue, newValue)    -> updateStyle(nameField      , MANDATORY_STYLE, newValue));
        cantonProxy.capital.mandatoryProperty().addListener((observable, oldValue, newValue)     -> updateStyle(capitalField, MANDATORY_STYLE, newValue));
    }

    @Override
    public void setupBindings() {
        //setupBindings_DolphinBased();
        setupBindings_VeneerBased();
    }

    private void setupBindings_DolphinBased() {
        // you can fetch all existing PMs from the modelstore via clientDolphin
        ClientPresentationModel cantonProxyPM = clientDolphin.getAt(BasePmMixin.CANTON_PROXY_PM_ID);

        //JFXBinder is ui toolkit agnostic. We have to use Strings
        JFXBinder.bind(CantonAtt.NAME.name())
                 .of(cantonProxyPM)
                 .using(value -> value + ", " + cantonProxyPM.getAt(CantonAtt.NAME.name()).getValue())
                 .to("text")
                 .of(headerLabel);

        JFXBinder.bind(CantonAtt.CAPITAL.name())
                 .of(cantonProxyPM)
                 .using(value -> cantonProxyPM.getAt(CantonAtt.CAPITAL.name()).getValue() + ", " + value)
                 .to("text")
                 .of(headerLabel);


        JFXBinder.bind(CantonAtt.NAME.name(), Tag.LABEL).of(cantonProxyPM).to("text").of(nameLabel);
        JFXBinder.bind(CantonAtt.NAME.name()).of(cantonProxyPM).to("text").of(nameField);
        JFXBinder.bind("text").of(nameField).to(CantonAtt.NAME.name()).of(cantonProxyPM);

        JFXBinder.bind(CantonAtt.CAPITAL.name(), Tag.LABEL).of(cantonProxyPM).to("text").of(capitalLabel);
        JFXBinder.bind(CantonAtt.CAPITAL.name()).of(cantonProxyPM).to("text").of(capitalField);
        Converter toIntConverter = value -> {
            try {
                int newValue = Integer.parseInt(value.toString());
                cantonProxyPM.getAt(CantonAtt.NAME.name(), AdditionalTag.VALID).setValue(true);
                cantonProxyPM.getAt(CantonAtt.NAME.name(), AdditionalTag.VALIDATION_MESSAGE).setValue("OK");

                return newValue;
            } catch (NumberFormatException e) {
                cantonProxyPM.getAt(CantonAtt.NAME.name(), AdditionalTag.VALID).setValue(false);
                cantonProxyPM.getAt(CantonAtt.NAME.name(), AdditionalTag.VALIDATION_MESSAGE).setValue("Not a number");
                return cantonProxyPM.getAt(CantonAtt.NAME.name()).getValue();
            }
        };
        JFXBinder.bind("text").of(capitalField).using(toIntConverter).to(CantonAtt.CAPITAL.name()).of(cantonProxyPM);

        Converter not = value -> !(boolean) value;
        JFXBinder.bindInfo(Attribute.DIRTY_PROPERTY).of(cantonProxyPM).using(not).to("disable").of(saveButton);
        JFXBinder.bindInfo(Attribute.DIRTY_PROPERTY).of(cantonProxyPM).using(not).to("disable").of(resetButton);

        PresentationModel presentationStatePM = clientDolphin.getAt(BasePmMixin.APPLICATION_STATE_PM_ID);

        JFXBinder.bind(ApplicationStateAtt.LANGUAGE.name()).of(presentationStatePM).using(value -> value.equals(Language.GERMAN.name())).to("disable").of(germanButton);
        JFXBinder.bind(ApplicationStateAtt.LANGUAGE.name()).of(presentationStatePM).using(value -> value.equals(Language.ENGLISH.name())).to("disable").of(englishButton);
    }

    private void setupBindings_VeneerBased(){
       headerLabel.textProperty().bind(cantonProxy.name.valueProperty().concat(", ").concat(cantonProxy.capital.valueProperty()));

        idLabel.textProperty().bind(cantonProxy.id.labelProperty());
        idField.textProperty().bind(cantonProxy.id.valueProperty().asString());

        setupBinding(nameLabel   , nameField      , cantonProxy.name);
        setupBinding(capitalLabel, capitalField, cantonProxy.capital);

        germanButton.disableProperty().bind(Bindings.createBooleanBinding(() -> Language.GERMAN.equals(ps.language.getValue()), ps.language.valueProperty()));
        englishButton.disableProperty().bind(Bindings.createBooleanBinding(() -> Language.ENGLISH.equals(ps.language.getValue()), ps.language.valueProperty()));

        saveButton.disableProperty().bind(cantonProxy.dirtyProperty().not());
        resetButton.disableProperty().bind(cantonProxy.dirtyProperty().not());
    }

    private void setupBinding(Label label, TextField field, AttributeFX attribute) {
        setupBinding(label, attribute);

        field.textProperty().bindBidirectional(attribute.userFacingStringProperty());
        field.tooltipProperty().bind(Bindings.createObjectBinding(() -> new Tooltip(attribute.getValidationMessage()),
                                                                  attribute.validationMessageProperty()
                                                                 ));
    }

    private void setupBinding(Label label, CheckBox checkBox, BooleanAttributeFX attribute) {
        setupBinding(label, attribute);
        checkBox.selectedProperty().bindBidirectional(attribute.valueProperty());
    }

    private void setupBinding(Label label, AttributeFX attribute){
        label.textProperty().bind(Bindings.createStringBinding(() -> attribute.getLabel() + (attribute.isMandatory() ? " *" : "  "),
                                                               attribute.labelProperty(),
                                                               attribute.mandatoryProperty()));
    }

    private void updateStyle(Node node, String style, boolean value){
        if(value){
            node.getStyleClass().add(style);
        }
        else {
            node.getStyleClass().remove(style);
        }
    }
}
