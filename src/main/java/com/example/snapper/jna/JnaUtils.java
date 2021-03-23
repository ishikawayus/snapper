package com.example.snapper.jna;

import java.awt.Rectangle;

import com.example.snapper.constant.KeyCode;
import com.example.snapper.jna.library.Dwmapi;
import com.example.snapper.jna.library.User32;
import com.example.snapper.jna.structure.Rect;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JnaUtils {

    public static int getForegroundWindow() {
        return User32.INSTANCE.GetForegroundWindow();
    }

    public static Rectangle getWindowRect(int hwnd) {
        Rect rect = new Rect();
        Dwmapi.INSTANCE.DwmGetWindowAttribute(hwnd, DwmWindowAttribute.DWMWA_EXTENDED_FRAME_BOUNDS, rect,
                rect.size());
        return new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
    }

    public static boolean getAsyncKeyState(KeyCode keyCode) {
        short state = User32.INSTANCE.GetAsyncKeyState(keyCode.getJnaValue());
        return state != 0;
    }

    private static class DwmWindowAttribute {

        public static final int DWMWA_EXTENDED_FRAME_BOUNDS = 9;
    }
}
