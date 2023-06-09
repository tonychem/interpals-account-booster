package ru.yandex.tonychem.interpalsviewbooster.util;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.stage.Stage;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.Country;
import ru.yandex.tonychem.interpalsviewbooster.search.SearchUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AppUtils {
    public static void closeWindow(Event event) {
        Stage currentStage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
        currentStage.close();
    }

    public static List<Country> readCountryList() {
        List<Country> countries = new ArrayList<>(251);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        SearchUI.class.getResourceAsStream("countrylist.datafile")
                ))) {
            while (reader.ready()) {
                String[] inputString = reader.readLine().split(">");
                countries.add(new Country(inputString[1], inputString[0]));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error opening countrylist datafile");
        }

        return countries;
    }
}
