package br.com.matteusmoreno.garage_manager.utils;

import br.com.safeguard.check.SafeguardCheck;
import br.com.safeguard.types.ParametroTipo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("Utils Service Tests")
@ExtendWith(MockitoExtension.class)
class UtilsServiceTest {

    UtilsService utilsService = new UtilsService();

    @Test
    @DisplayName("Should calculate age correctly")
    void shouldCalculateAgeCorrectly() {
        String birthDateStr = "28/08/1990";
        Integer expectedAge = LocalDate.now().getYear() - 1990;

        Integer calculatedAge = utilsService.calculateAge(birthDateStr);

        assertEquals(expectedAge, calculatedAge);
    }

    @Test
    @DisplayName("Should validate CPF correctly")
    void shouldValidateCPFCorrectly() {
        String cpf = "421.871.600-50";

        Boolean isValid = utilsService.cpfValidation(cpf);

        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should invalidate incorrect CPF")
    void shouldInvalidateIncorrectCPF() {
        String invalidCpf = "123.456.789-00";

        Boolean isValid = utilsService.cpfValidation(invalidCpf);

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should validate date correctly")
    void shouldValidateDateCorrectly() {
        String birthDateStr = "28/08/1990";

        Boolean dateHasError = utilsService.dateValidation(birthDateStr);

        assertTrue(dateHasError);
    }

    @Test
    @DisplayName("Should invalidate incorrect date")
    void shouldInvalidateIncorrectDate() {
        String invalidDate = "30/02/1990";

        Boolean dateHasError = utilsService.dateValidation(invalidDate);

        assertFalse(dateHasError);
    }

}