package org.example.peluqueria.infraestructure.utils;

import org.example.peluqueria.exceptions.EmailNotValidException;
import org.example.peluqueria.exceptions.InvalidPasswordException;

import java.util.regex.Pattern;

public class CredentialUtils {

    public static void validatePassword(String password) {

        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$";

        if (!password.matches(pattern)) {

            throw new InvalidPasswordException(
                    "Password must be at least 8 characters and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
        }

    }

    public static void validateEmail(String email) {

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern p = Pattern.compile(emailRegex);

        if (!p.matcher(email).matches()) {

            throw new EmailNotValidException("Email is not valid");
        }

    }
}
