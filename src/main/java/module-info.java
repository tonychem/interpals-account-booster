module ru.yandex.tonychem.interpalsviewbooster {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;


    opens ru.yandex.tonychem.interpalsviewbooster to javafx.fxml;
    exports ru.yandex.tonychem.interpalsviewbooster;

    exports ru.yandex.tonychem.interpalsviewbooster.login;
    opens ru.yandex.tonychem.interpalsviewbooster.login to javafx.fxml;
}