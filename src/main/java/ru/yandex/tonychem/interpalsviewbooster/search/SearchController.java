package ru.yandex.tonychem.interpalsviewbooster.search;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import ru.yandex.tonychem.interpalsviewbooster.engine.Country;
import ru.yandex.tonychem.interpalsviewbooster.util.AppUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class SearchController implements Initializable {

    @FXML
    private ComboBox<Integer> ageFromChoiceBox;

    @FXML
    private ComboBox<Integer> ageToChoiceBox;

    @FXML
    private ComboBox<Country> countryChoiceBox;

    @FXML
    private Slider delaySlider;

    @FXML
    private Label dynamicDelayLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Integer[] ageRange = IntStream.rangeClosed(13, 110).boxed().toArray(Integer[]::new);
        List<Country> countries = AppUtils.readCountryList();
        ChangeListener<Number> changeListener = (var1, var2, var3) -> {
            int delayValue = (int) delaySlider.getValue();
            dynamicDelayLabel.setText(delayValue + " ms");
        };

        countryChoiceBox.getItems().setAll(countries);
        ageFromChoiceBox.getItems().setAll(ageRange);
        ageToChoiceBox.getItems().setAll(ageRange);
        delaySlider.valueProperty().addListener(changeListener);

        ageFromChoiceBox.getSelectionModel().selectFirst();
        ageToChoiceBox.getSelectionModel().selectLast();
        countryChoiceBox.getSelectionModel().selectFirst();
    }
}
