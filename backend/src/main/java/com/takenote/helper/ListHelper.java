package com.takenote.helper;

import com.takenote.exception.BadRequestException;

import java.util.List;
import java.util.Optional;

public class ListHelper {

    /**
     * This helper function is to get the first item of the list. stream().findFirst() returns an Optional field
     * which can be cumbersome when implementing this inside the service because of the required validation to check
     * if it is empty or not. This function is to save the time or extra line of codes from validating and converting the
     * Optional.
     * @param items     This is the list object that the function will try to get the first item from
     * @param <T>       This is the type of the list
     * @return This function will return the first item back to the caller
     */
    public static <T> T getFirst(List<T> items) {
        Optional<T> first = items.stream().findFirst();
        if (first.isEmpty()) {
            throw new BadRequestException("Unable to get first item from the list");
        }
        return first.orElseThrow();
    }
}
