package me.kate.lobby.utils;

public class MathUtil {

    // Avoiding Math.pow due to how resource intensive it is.
    public static double square(double val) {
        return val * val;
    }
    
}
