package br.com.matteusmoreno.garage_manager.motorcycle.constant;

import lombok.Getter;

@Getter
public enum MotorcycleBrand {
    AVELLOZ("Avelloz"),
    BAJAJ("Bajaj"),
    BMW("BMW"),
    BULL("Bull"),
    DAFRA("Dafra"),
    DUCATI("Ducati"),
    HAOJUE("Haojue"),
    HARLEY_DAVIDSON("Harley-Davidson"),
    HONDA("Honda"),
    HUSQVARNA("Husqvarna"),
    KAWASAKI("Kawasaki"),
    KTM("KTM"),
    LIFAN("Lifan"),
    ROYAL_ENFIELD("Royal Enfield"),
    SHINERAY("Shineray"),
    SUZUKI("Suzuki"),
    TRIUMPH("Triumph"),
    YAMAHA("Yamaha"),
    ZONTES("Zontes"),
    OTHER("Outra");

    private final String displayName;

    MotorcycleBrand(String displayName) {
        this.displayName = displayName;
    }

}
