package com.tuyenmonkey.mkloader.exception;

public class InvalidNumberOfPulseException extends Exception {
    public String getMessage() {
        return "The number of pulse must be between 2 and 6";
    }
}
