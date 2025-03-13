package br.com.matteusmoreno.garage_manager.domain;

import br.com.matteusmoreno.garage_manager.client.AddressApiResponse;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Address {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "zip_code")
    private String zipCode;
    private String street;
    private String neighborhood;
    private String number;
    private String city;
    private String state;
    private String complement;

    public Address(AddressApiResponse apiResponse) {
        this.zipCode = apiResponse.cep();
        this.street = apiResponse.localidade();
        this.neighborhood = apiResponse.bairro();
        this.city = apiResponse.logradouro();
        this.state = apiResponse.uf();
    }
}
