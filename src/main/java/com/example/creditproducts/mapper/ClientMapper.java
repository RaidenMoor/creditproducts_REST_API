package com.example.restbank.mapper;


import com.example.restbank.dto.ClientDTO;
import com.example.restbank.model.Client;
import com.example.restbank.model.CreditApplication;
import com.example.restbank.model.User;
import com.example.restbank.repository.ClientRepository;
import com.example.restbank.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper extends GenericMapper<Client, ClientDTO>
        implements ConverterForSpecificFields<Client, ClientDTO>{

    ClientRepository clientRepository;
    UserRepository userRepository;

    public ClientMapper() {
        super(Client.class, ClientDTO.class);
    }

    @PostConstruct
    @Override
    public void setupMapper() {

        modelMapper.createTypeMap(Client.class, ClientDTO.class)
                .addMappings(m -> {
                    m.skip(ClientDTO::setUserId);
                })
                .setPostConverter(toDtoConverter());

        modelMapper.createTypeMap(ClientDTO.class, Client.class)
                .addMappings(m -> {
                    m.skip(Client::setUser);

                })
                .setPostConverter(toEntityConverter());

    }

    @Override
    public void mapSpecificFields(ClientDTO source, Client destination) {
        Long userId = source.getUserId();
        if (userId != null) {
            destination.setUser(userRepository.
                    findById(userId).orElse(null));
        } else destination.setUser(null);

    }

    @Override
    public void mapSpecificFields(Client source, ClientDTO destination) {
        Long userId = null;
        User user = source.getUser();
        if (user != null) {
            userId = user.getId();
        }
        destination.setUserId(userId);


    }
}
