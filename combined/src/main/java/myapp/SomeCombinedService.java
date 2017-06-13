package myapp;

import myapp.presentationmodel.canton.CantonAtt;
import myapp.service.SomeService;
import myapp.util.DTOMixin;
import org.opendolphin.core.server.DTO;

import java.util.List;
import java.util.Random;

public class SomeCombinedService implements SomeService, DTOMixin {

    String[] cantons = {"Aargau", "Appenzell-Ausserrhoden", "Appenzell-Innerrhoden", "Basel-Land",
            "Basel-Stadt", "Bern", "Fribourg", "Genf", "Glarus", "Graubünden",  "Jura", "Luzern",
            "Neuenburg", "Nidwalden", "Obwalden", "Schaffhausen", "Schwyz", "Solothurn",  "St.Gallen",
            "Thurgau ",  "Tessin", "Uri", "Wallis", "Waadt", "Zug", "Zürich",
                };

   /* String[] capitals = {"Aarau", "Herisau", "Appenzell", "Liestal",
            "Basel", "Bern", "Fribourg", "Genf" , "Glarus", "Chur", "Delémont", "Luzern",
            "Neuchâtel", "Stans", "Sarnen", "Schaffhausen", "Schwyz", "Solothurn", "St. Gallen",
            "Frauenfeld ", "Bellinzona", "Altdorf", "Sion", "Lausanne", "Zug", "Zürich",};*/

    @Override
    public DTO loadSomeEntity() {
        long id = createNewId();

        Random r        = new Random();
        int cantonToDisplay = r.nextInt(cantons.length);
        String name     = cantons[cantonToDisplay];
        //String captial  = capitals[cantonToDisplay];


        return new DTO(createSlot(CantonAtt.ID      , id     , id),
                       createSlot(CantonAtt.NAME    , name   , id)
                       //createSlot(CantonAtt.CAPITAL , captial , id)
                       );
    }

    @Override
    public void save(List<DTO> dtos) {
        System.out.println(" Data to be saved");
        dtos.stream()
            .flatMap(dto -> dto.getSlots().stream())
            .map(slot -> String.join(", ", slot.getPropertyName(), slot.getValue().toString(), slot.getQualifier()))
            .forEach(System.out::println);
    }
}
