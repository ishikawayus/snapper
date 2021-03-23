package com.example.snapper.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum KeyCode {

    VK_SHIFT(0x10),
    VK_CONTROL(0x11),
    VK_MENU(0x12),
    VK_NONCONVERT(0x1D);

    @Getter
    private final int jnaValue;
}
