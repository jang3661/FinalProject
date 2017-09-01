package com.example.doublejk.laboum.tools;

import com.example.doublejk.laboum.util.CustomBus;

/**
 * Created by doublejk on 2017-08-27.
 */

public class PlayerControlProvider {
    private static final CustomBus BUS = new CustomBus();

    public static CustomBus getInstance() {
        return BUS;
    }

    private PlayerControlProvider() { }
}
