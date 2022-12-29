package com.goldsikka.goldsikka.Enum;

import android.graphics.Color;

import com.goldsikka.goldsikka.R;

public enum Passwordcalculation_Enum {

    WEAK(R.string.weak, Color.parseColor("#FF0000")),
    MEDIUM(R.string.medium, Color.parseColor("#FFFF99")),
    STRONG(R.string.strong, Color.parseColor("#90EE90"));
    public int msg;
    public int color;
    private static int MIN_LENGTH = 6;
    private static int MAX_LENGTH = 15;
    private static int MEDIUM_LENGTH=8;

    Passwordcalculation_Enum(int msg, int color) {
        this.msg = msg;
        this.color = color;
    }

    public static Passwordcalculation_Enum calculate(String password) {
        int score = 0;
        // boolean indicating if password has an upper case
        boolean upper = false;
        // boolean indicating if password has a lower case
        boolean lower = false;
        // boolean indicating if password has at least one digit
        boolean digit = false;
        // boolean indicating if password has a leat one special char
        boolean specialChar = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (!specialChar  &&  !Character.isLetterOrDigit(c)) {
                score++;
                specialChar = true;
            } else {
                if (!digit  &&  Character.isDigit(c)) {
                    score++;
                    digit = true;
                } else {
                    if (!upper || !lower) {
                        if (Character.isUpperCase(c)) {
                            upper = true;
                        } else {
                            lower = true;
                        }

                        if (upper && lower) {
                            score++;
                        }
                    }
                }
            }
        }

        int length = password.length();

        if (length > MAX_LENGTH) {
            score++;
        } else if (length < MIN_LENGTH) {
            score = 0;
        }
        else if(length > MEDIUM_LENGTH){
            score++;
        }

        // return enum following the score
        switch(score) {
            case 0 : return WEAK;
            case 1 : return MEDIUM;
            case 2 : return STRONG;
//            case 3 : return VERY_STRONG;
            default:
        }

        return STRONG;
    }
}
