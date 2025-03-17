package br.com.matteusmoreno.garage_manager.utils;

import br.com.safeguard.check.SafeguardCheck;
import br.com.safeguard.interfaces.Check;
import br.com.safeguard.types.ParametroTipo;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

@ApplicationScoped
public class UtilsService {

    public Integer calculateAge(String birthDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate birthDate = LocalDate.parse(birthDateStr, formatter);
        LocalDate currentDate = LocalDate.now();

        return currentDate.getYear() - birthDate.getYear();
    }

    public Boolean cpfValidation(String cpf) {
        Check check = new SafeguardCheck();
        return !check.elementOf(cpf, ParametroTipo.CPF).validate().hasError();
    }


    public Boolean dateValidation(String birthDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                .withResolverStyle(ResolverStyle.STRICT)
                .withLocale(Locale.getDefault());
        try {
            LocalDate.parse(birthDateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
