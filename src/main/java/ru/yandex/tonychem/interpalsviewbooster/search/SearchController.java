package ru.yandex.tonychem.interpalsviewbooster.search;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import ru.yandex.tonychem.interpalsviewbooster.configuration.BeansHolder;
import ru.yandex.tonychem.interpalsviewbooster.engine.CrawlEngine;
import ru.yandex.tonychem.interpalsviewbooster.engine.ScrapeAndVisitTask;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.Country;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.Sex;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.UserSearchQuery;
import ru.yandex.tonychem.interpalsviewbooster.util.AppUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class SearchController implements Initializable {

    private CrawlEngine engine = BeansHolder.engine();
    private ExecutorService executorService = BeansHolder.executorService();

    private volatile ConcurrentLinkedQueue<Object> consoleLoggingQueue = new ConcurrentLinkedQueue<>();

    private Task<Void> scrapeAndVisitTask, progressBarUpdateTask, consoleUpdateTask;

    @FXML
    private ComboBox<Integer> ageFromChoiceBox, ageToChoiceBox;

    @FXML
    private ComboBox<Country> countryChoiceBox;

    @FXML
    private Slider delaySlider;

    @FXML
    private Label dynamicDelayLabel, crawlProfilesLabel;

    @FXML
    private CheckBox onlineCheckBox, visitPreviouslyViewedCheckBox;

    @FXML
    private RadioButton maleSexButton, femaleSexButton;

    @FXML
    private AnchorPane queryPane;

    @FXML
    private ProgressBar scrapeIndicator;

    @FXML
    private Button initiateBoostButton, cancelBoostButton;

    @FXML
    private TextArea consoleArea;

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
        UserSearchQuery userSearchQuery = readUIFields();

        queryPane.setOpacity(0.23d);
        queryPane.setCursor(Cursor.WAIT);
        initiateBoostButton.setVisible(false);

        cancelBoostButton.setVisible(true);
        scrapeIndicator.setVisible(true);
        crawlProfilesLabel.setVisible(true);

        AtomicReference<Double> visitedAccountsProgress = new AtomicReference<>(0.0d);

        scrapeAndVisitTask = new ScrapeAndVisitTask(engine, userSearchQuery, visitedAccountsProgress,
                consoleLoggingQueue);
        progressBarUpdateTask = new ProgressBarUpdateTask(scrapeIndicator, visitedAccountsProgress);
        consoleUpdateTask = new ConsoleUpdateTask(consoleArea, consoleLoggingQueue);

        EventHandler<WorkerStateEvent> doneOrCanceledSearchPaneHandler = (stateEvent) -> {
            queryPane.setOpacity(1.0d);
            queryPane.setCursor(Cursor.DEFAULT);
            cancelBoostButton.setVisible(false);
            initiateBoostButton.setVisible(true);
        };

        scrapeAndVisitTask.setOnSucceeded(doneOrCanceledSearchPaneHandler);
        scrapeAndVisitTask.setOnCancelled(doneOrCanceledSearchPaneHandler);

        EventHandler<WorkerStateEvent> doneOrCanceledFooterPaneHandler = (stateEvent) -> {
            crawlProfilesLabel.setVisible(false);
            scrapeIndicator.setVisible(false);
        };

        progressBarUpdateTask.setOnSucceeded(doneOrCanceledFooterPaneHandler);
        progressBarUpdateTask.setOnCancelled(doneOrCanceledFooterPaneHandler);

        executorService.submit(consoleUpdateTask);
        executorService.submit(progressBarUpdateTask);
        executorService.submit(scrapeAndVisitTask);
    }

    public void cancelSearch(ActionEvent event) {
        scrapeAndVisitTask.cancel(true);
        progressBarUpdateTask.cancel(true);
        consoleUpdateTask.cancel(true);
    }

    private UserSearchQuery readUIFields() {
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

        return new UserSearchQuery(ageStart, ageEnd, sex, country,
                onlineOnly, visitPreviouslyViewedAccounts, requestDelay);
    }
}
