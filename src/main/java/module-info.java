module ru.yandex.tonychem.interpalsviewbooster {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires org.apache.commons.lang3;


    opens ru.yandex.tonychem.interpalsviewbooster to javafx.fxml;
    exports ru.yandex.tonychem.interpalsviewbooster;

    exports ru.yandex.tonychem.interpalsviewbooster.login;
    opens ru.yandex.tonychem.interpalsviewbooster.login to javafx.fxml;

    exports ru.yandex.tonychem.interpalsviewbooster.search;
    opens ru.yandex.tonychem.interpalsviewbooster.search to javafx.fxml;

    exports ru.yandex.tonychem.interpalsviewbooster.about;
    opens ru.yandex.tonychem.interpalsviewbooster.about to javafx.fxml;

    exports ru.yandex.tonychem.interpalsviewbooster.task;
    opens ru.yandex.tonychem.interpalsviewbooster.task to javafx.fxml;
}