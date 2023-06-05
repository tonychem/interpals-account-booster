package ru.yandex.tonychem.interpalsviewbooster;

import ru.yandex.tonychem.interpalsviewbooster.login.LoginUIMain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InterpalsBoosterApplication {
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(() -> LoginUIMain.main(args));
        service.shutdown();
    }
}
