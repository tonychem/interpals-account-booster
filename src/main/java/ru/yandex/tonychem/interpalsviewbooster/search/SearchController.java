package ru.yandex.tonychem.interpalsviewbooster.search;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ru.yandex.tonychem.interpalsviewbooster.configuration.BeansHolder;
import ru.yandex.tonychem.interpalsviewbooster.engine.CrawlEngine;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.Account;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.Country;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.Sex;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.UserSearchQuery;
import ru.yandex.tonychem.interpalsviewbooster.util.AppUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.IntStream;

public class SearchController implements Initializable {

    private CrawlEngine engine = BeansHolder.engine();

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

    @FXML
    private CheckBox onlineCheckBox;

    @FXML
    private CheckBox visitPreviouslyViewedCheckBox;

    @FXML
    private RadioButton bothSexButton, maleSexButton, femaleSexButton;

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


    public void initiateSearch(ActionEvent event) {
        int ageStart = ageFromChoiceBox.getValue();
        int ageEnd = ageToChoiceBox.getValue();
        Country country = countryChoiceBox.getValue();
        boolean onlineOnly = onlineCheckBox.isSelected();
        boolean visitPreviouslyViewedAccounts = visitPreviouslyViewedCheckBox.isSelected();
        int requestDelay = (int) delaySlider.getValue();

        Sex sex = Sex.BOTH;

        if (maleSexButton.isSelected()) {
            sex = Sex.MALE;
        } else if (femaleSexButton.isSelected()) {
            sex = Sex.FEMALE;
        }

        Set<Account> accountsToBeVisited = engine.gatherAccounts(new UserSearchQuery(ageStart, ageEnd, sex, country,
                onlineOnly, visitPreviouslyViewedAccounts, requestDelay));


    }
}
