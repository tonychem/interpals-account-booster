package ru.yandex.tonychem.interpalsviewbooster.util;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.commons.lang3.SerializationUtils;
import ru.yandex.tonychem.interpalsviewbooster.InterpalsBoosterApplication;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.Account;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.Country;
import ru.yandex.tonychem.interpalsviewbooster.search.SearchUI;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class AppUtils {

    public static final String QUEUE_POISON_PILL = "POISON_PILL";
    public static final String BAD_CREDENTIALS_FLAG = "If you have an account, please sign in below";
    public static final String DENIAL_SERVER_RESPONSE =
            "You have been visiting too many profiles in a short period of time.";

    public static final String DENIAL_CLIENT_RESPONSE = "Account has been put on hold " +
            "due to too many requests. Please wait 30 s before next attempt";

    public static final int DENIAL_WAIT = 30_000;

    public static final Path CACHE_FILE_PATH = Paths.get("cache", "visited-profiles.cache");

    public static void closeWindow(Event event) {
        Stage currentStage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
        currentStage.close();
    }

    public static void closeWindow(Node node) {
        Stage currentStage = (Stage) node.getScene().getWindow();
        currentStage.close();
    }

    public static Alert alert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        Stage alertStage = ((Stage) (alert.getDialogPane().getScene().getWindow()));
        Image alertIcon = new Image(InterpalsBoosterApplication.class.getResourceAsStream("logo.png"));
        alertStage.getIcons().add(alertIcon);

        alert.setTitle("Exit");
        alert.setHeaderText("You are closing the application");
        alert.setContentText("Are you sure you want to exit?");

        return alert;
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

        Collections.sort(countries.subList(1, countries.size()));
        return countries;
    }

    public static List<String> readUserAgents() {
        List<String> userAgentList = new ArrayList<>(247);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        InterpalsBoosterApplication.class.getResourceAsStream("user-agent.datafile")
                ))) {
            while (reader.ready()) {
                userAgentList.add(reader.readLine());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error opening user-agent datafile");
        }

        return userAgentList;
    }

    public static void createCacheFile() {
        try {
            if (!Files.exists(CACHE_FILE_PATH)) {
                Files.createDirectory(CACHE_FILE_PATH.getParent());
                Files.createFile(CACHE_FILE_PATH);

                try (FileOutputStream fos = new FileOutputStream(CACHE_FILE_PATH.toFile())) {
                    SerializationUtils.serialize(new HashMap<String, Set<Account>>(), fos);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Set<Account>> loadVisitedAccounts() {
        try (FileInputStream fis = new FileInputStream(CACHE_FILE_PATH.toFile())) {
            return SerializationUtils.deserialize(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
