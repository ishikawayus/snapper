package com.example.snapper.jna.library;

import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public interface Dwmapi extends StdCallLibrary {

    Dwmapi INSTANCE = Native.load("dwmapi", Dwmapi.class, W32APIOptions.UNICODE_OPTIONS);

    int DwmGetWindowAttribute(int hwnd, int dwAttribute, Structure pvAttribute, int cbAttribute);
}
