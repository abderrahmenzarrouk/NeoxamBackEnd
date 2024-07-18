package com.example.neoxamBack.security;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Pattern;
@Service
public class emailValidator implements Predicate<String> {

    private static final String EMAIL_REGEX =
            "^[a-zA-Z][a-zA-Z0-9]*@[a-zA-Z0-9]+\\.[a-zA-Z]{2,7}$";

    private final Pattern pattern;


    public emailValidator() {
        this.pattern = Pattern.compile(EMAIL_REGEX);
    }

    @Override
    public boolean test(String s) {
        return pattern.matcher(s).matches();
    }
}
