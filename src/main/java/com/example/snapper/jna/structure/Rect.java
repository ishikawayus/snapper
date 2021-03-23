package com.example.snapper.jna.structure;

import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

@FieldOrder({ "left", "top", "right", "bottom" })
public class Rect extends Structure {

    public int left;
    public int top;
    public int right;
    public int bottom;
}
