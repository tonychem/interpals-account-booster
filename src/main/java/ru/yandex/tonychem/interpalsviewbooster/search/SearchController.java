package ru.yandex.tonychem.interpalsviewbooster.search;

import javafx.application.Platform;
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
import javafx.scene.layout.Pane;
import ru.yandex.tonychem.interpalsviewbooster.about.AboutUI;
import ru.yandex.tonychem.interpalsviewbooster.beans.BeansHolder;
import ru.yandex.tonychem.interpalsviewbooster.engine.CacheManager;
import ru.yandex.tonychem.interpalsviewbooster.engine.CrawlEngine;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.Country;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.Sex;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.UserSearchQuery;
import ru.yandex.tonychem.interpalsviewbooster.task.ConsoleUpdateTask;
import ru.yandex.tonychem.interpalsviewbooster.task.ScrapeAndVisitTask;
import ru.yandex.tonychem.interpalsviewbooster.util.AppUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

public class SearchController implements Initializable {

    private CrawlEngine engine = BeansHolder.engine();
    private ExecutorService executorService = BeansHolder.EXECUTOR_SERVICE;

    private LinkedBlockingQueue<Object> consoleLoggingQueue = new LinkedBlockingQueue<>();

    private CacheManager cacheManager = BeansHolder.sessionCacheManager;

    private Task<Void> scrapeAndVisitTask, consoleUpdateTask;

    private EventHandler<WorkerStateEvent> doneOrCanceledScrapeAndVisitEvent;

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
    private Pane mainWindowPane;

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

        doneOrCanceledScrapeAndVisitEvent = (stateEvent) -> {
            queryPane.setOpacity(1.0d);
            queryPane.setCursor(Cursor.DEFAULT);
            cancelBoostButton.setVisible(false);
            initiateBoostButton.setVisible(true);
            crawlProfilesLabel.setVisible(false);
            scrapeIndicator.setVisible(false);
        };
    }

    //Main method of the application. Method initiates search&scraping
    public void initiateSearch(ActionEvent event) {
        UserSearchQuery userSearchQuery = readUIFields();

        setPaneToWaitingState();

        consoleLoggingQueue.clear();

        scrapeAndVisitTask = new ScrapeAndVisitTask(engine, userSearchQuery, cacheManager, consoleLoggingQueue);
        consoleUpdateTask = new ConsoleUpdateTask(consoleArea, consoleLoggingQueue);

        scrapeAndVisitTask.setOnSucceeded(doneOrCanceledScrapeAndVisitEvent);
        scrapeAndVisitTask.setOnCancelled(doneOrCanceledScrapeAndVisitEvent);
        scrapeIndicator.progressProperty().bind(scrapeAndVisitTask.progressProperty());

        executorService.submit(consoleUpdateTask);
        executorService.submit(scrapeAndVisitTask);
    }

    public void terminateSearch(ActionEvent actionEvent) {
        if (scrapeAndVisitTask != null) {
            scrapeAndVisitTask.cancel(true);
        }

        if (consoleUpdateTask != null) {
            consoleUpdateTask.cancel(true);
        }

        cacheManager.flush();
    }

    private void setPaneToWaitingState() {
        queryPane.setOpacity(0.23d);
        queryPane.setCursor(Cursor.WAIT);
        initiateBoostButton.setVisible(false);

        cancelBoostButton.setVisible(true);
        scrapeIndicator.setVisible(true);
        crawlProfilesLabel.setVisible(true);
    }

    public void exit(ActionEvent event) {
        Alert alert = AppUtils.alert();

        if (alert.showAndWait().get() == ButtonType.OK) {
            cacheManager.flush();
            AppUtils.closeWindow(mainWindowPane);
            System.exit(0);
        } else {
            event.consume();
        }
    }

    public void clearCache(ActionEvent event) {
        if (scrapeAndVisitTask == null || !scrapeAndVisitTask.isRunning()) {
            cacheManager.deleteCache();
            cacheManager.flush();
        }
    }

    public void aboutMenu(ActionEvent event) {
        Platform.runLater(AboutUI::renderWindow);
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
