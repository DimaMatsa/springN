package com.matsa.entity;

import com.matsa.dto.ClientDto;
import lombok.*;
import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Client{
    @Id
    @SequenceGenerator(name = "books", sequenceName = "books")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "books")
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public Client(ClientDto dto) {
        firstName = dto.getFirstName();
        lastName = dto.getLastName();
        email = dto.getEmail();
        phone = dto.getPhone();
    }
}
