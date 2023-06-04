module ru.yandex.tonychem.interpalsviewbooster {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens ru.yandex.tonychem.interpalsviewbooster to javafx.fxml;
    exports ru.yandex.tonychem.interpalsviewbooster;

    opens ru.yandex.tonychem.interpalsviewbooster.controller to javafx.fxml;
    exports ru.yandex.tonychem.interpalsviewbooster.controller;
}