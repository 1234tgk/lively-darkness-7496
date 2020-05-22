package com.example.livelydarkness;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculateTime {
    private String file;

    public CalculateTime(String input) {
        file = input;
    }

    /**
     * Given the string generated from fileToStr(),
     * the function now calculates the time.
     * @return int[], length of two
     */
    public long[] calculateTimeDiff() {
        long[] ret = new long[2];
        /*
         This return integer-array will have length of two.
         ret[0] will have the time user was inside of the geoscape radius.
         ret[1] will have the time user was outside of the geoscape radius.
         */

        boolean isInside = true;
        /*
         boolean where if true, calculate inside time (ret[0]),
         false, calculate outside time (ret[1]).
         */

        // check what is the initial status: inside or outside of the radius
        if (file.substring(0, 5).equals("EXIT"))
            isInside = false;
        // this method may cause error if the
        // very first part of the string is not "ENTER" or "EXIT".

        /*
         String has four components: "ENTER", "EXIT", numbers of epoch seconds,
            and possible newline character.
         File will look something like this: "ENTER \d* \n EXIT \d* \n ENTER \d* \n EXIT \d \n ...*
         Number alternates between enter time and exit time.
         Need to extract only the numbers from the string in order for the calculation to be
            possible.
         We will use regex to point-out only the numbers.
         Every time we find the number, we will alternate isInside variable, indicating whether
            number is in ENTER or EXIT. We will also increase the respective number of return array.
         */
        Pattern regex = Pattern.compile("\\d+"); // find a group of numbers
        Matcher numbers = regex.matcher(file);
        ArrayList<Long> list = new ArrayList<>();
        while (numbers.find()) {
            list.add(Long.parseLong(numbers.group()));
        }

        for (int j = 1 ; j < list.size() ; j++) {
            long diff = list.get(j) - list.get(j - 1);
            if (isInside) {
                ret[0] += diff;
                isInside = false;
            } else {
                ret[1] += diff;
                isInside = true;
            }
        }

        return ret;
    }
}
