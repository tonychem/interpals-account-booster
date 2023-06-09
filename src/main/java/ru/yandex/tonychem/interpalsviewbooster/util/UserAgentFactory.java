package ru.yandex.tonychem.interpalsviewbooster.util;

import java.util.List;
import java.util.Random;

public class UserAgentFactory {
    private final List<String> userAgents;
    private final Random rnd;

    public UserAgentFactory() {
        userAgents = AppUtils.readUserAgents();
        rnd = new Random();
    }

    public String getAgent() {
        int index = rnd.nextInt(userAgents.size());
        return userAgents.get(index);
    }
}
