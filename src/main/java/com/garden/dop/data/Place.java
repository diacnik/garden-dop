package com.garden.dop.data;

public sealed interface Place permits Yard, House {}
record Yard(long id, String name, int hardinessZone) implements Place {}
record House(long id, String name) implements Place {}
