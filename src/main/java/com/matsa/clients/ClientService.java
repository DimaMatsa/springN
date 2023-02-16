package com.matsa.clients;

import com.matsa.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Optional;
@Slf4j
@Service
public class ClientService {
    @Autowired
    public ClientRepository clientRepository;

    public Client get(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Client not found"));
    }

    public Page<Client> getAll(String query, Pageable pageable) {
        if (query != null) {
            return clientRepository.findByQuery("%" + query.toLowerCase() + "%", pageable);
        }
        return clientRepository.findAll(pageable);
    }

    public Client create(ClientDto dto) {
        Optional<Client> optional = clientRepository.findByEmailOrPhone(dto.getEmail(), dto.getPhone());
        if (optional.isPresent()){
            throw new ConflictException("Client already exists");
        }
        Client client = new Client(dto);
        Client saved = clientRepository.save(client);
        log.info("Client {} created", saved);
        return saved;
    }

    public Client update(Long id, ClientDto dto) {
        Client original = get(id);
        original.setFirstName(dto.getFirstName());
        original.setLastName(dto.getLastName());
        original.setEmail(dto.getEmail());
        original.setPhone(dto.getPhone());
        return clientRepository.save(original);
    }

    public void delete(Long id) {
        Client client = get(id);
        clientRepository.delete(client);
        log.info("Client {} successfully deleted", client);
    }
}
