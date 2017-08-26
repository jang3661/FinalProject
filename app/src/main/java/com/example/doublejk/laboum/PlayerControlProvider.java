package com.example.doublejk.laboum;

import com.squareup.otto.Bus;

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
