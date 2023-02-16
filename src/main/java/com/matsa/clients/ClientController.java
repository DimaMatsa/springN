package com.matsa.clients;

import com.matsa.Book;
import com.matsa.BookDto;
import com.matsa.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/client")
    private Page<Client> getAllClients(@RequestParam Integer page,
                                   @RequestParam Integer size,
                                   @RequestParam(required = false) String query) {
        Pageable pageable = PageRequest.of(page, size);
        return clientService.getAll(query, pageable);
    }

    @GetMapping("/client/{id}")
    public Client getClientById(@PathVariable Long id) {
        return clientService.get(id);
    }

    @PostMapping("/client")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Client createClient(@Valid @RequestBody ClientDto dto) {
        return clientService.create(dto);
    }

    @PutMapping("/client/{id}")
    public Client editClient(@PathVariable Long id,@Valid @RequestBody ClientDto dto) {
        return clientService.update(id, dto);
    }

    @DeleteMapping("/client/{id}")
    public void deleteClient(@PathVariable Long id) {
        clientService.delete(id);
    }
}
