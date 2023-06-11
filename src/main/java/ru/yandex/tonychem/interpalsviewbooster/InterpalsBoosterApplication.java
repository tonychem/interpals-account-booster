package ru.yandex.tonychem.interpalsviewbooster;

import ru.yandex.tonychem.interpalsviewbooster.login.LoginUI;
import ru.yandex.tonychem.interpalsviewbooster.util.AppUtils;

public class InterpalsBoosterApplication {
    public static void main(String[] args) {
        AppUtils.createCacheFile();
        LoginUI.main(args);
    }
}
