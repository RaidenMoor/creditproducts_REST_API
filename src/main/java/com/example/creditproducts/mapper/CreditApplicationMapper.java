package com.example.restbank.mapper;

import com.example.restbank.dto.CreditApplicationDTO;
import com.example.restbank.model.Client;
import com.example.restbank.model.CreditApplication;
import com.example.restbank.model.CreditProduct;

import com.example.restbank.repository.ClientRepository;
import com.example.restbank.repository.CreditApplicationRepository;
import com.example.restbank.repository.CreditProductRepository;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreditApplicationMapper extends GenericMapper<CreditApplication, CreditApplicationDTO>
    implements ConverterForSpecificFields<CreditApplication, CreditApplicationDTO>{
    public CreditApplicationMapper() {
        super(CreditApplication.class, CreditApplicationDTO.class);
    }

    CreditProductRepository creditProductRepository;
    CreditApplicationRepository creditApplicationRepository;
    ClientRepository clientRepository;

    @PostConstruct
    @Override
    public void setupMapper() {
        modelMapper.createTypeMap(CreditApplication.class, CreditApplicationDTO.class)
                .addMappings(m -> {
                    m.skip(CreditApplicationDTO::setClientId);
                    m.skip(CreditApplicationDTO::setCreditProductId);
                })
                .setPostConverter(toDtoConverter());

        modelMapper.createTypeMap(CreditApplicationDTO.class, CreditApplication.class)
                .addMappings(m -> {
                    m.skip(CreditApplication::setClient);
                    m.skip(CreditApplication::setCreditProduct);

                })
                .setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(CreditApplicationDTO source, CreditApplication destination) {
        Long creditProductId = source.getCreditProductId();
        if (creditProductId != null) {
            destination.setCreditProduct(creditProductRepository.
                    findById(creditProductId).orElse(null));
        } else destination.setCreditProduct(null);

        Long clientId = source.getClientId();
        if (clientId != null) {
            destination.setClient(clientRepository.
                    findById(clientId).orElse(null));
        } else destination.setClient(null);

    }

    @Override
    public void mapSpecificFields(CreditApplication source, CreditApplicationDTO destination) {
        Long creditProductId = null;
        CreditProduct creditProduct = source.getCreditProduct();
        if (creditProduct != null) {
            creditProductId = creditProduct.getId();
        }
        destination.setCreditProductId(creditProductId);

        Long userId = null;
        Client client = source.getClient();
        if (client != null) {
            userId = client.getId();
        }
        destination.setClientId(userId);

    }

    @Autowired
    public void setCreditProductRepository(CreditProductRepository creditProductRepository){
        this.creditProductRepository = creditProductRepository;
    }

    @Autowired
    public void setClientRepository(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

    @Autowired
    public void setCreditApplicationRepository(CreditApplicationRepository creditApplicationRepository){
        this.creditApplicationRepository = creditApplicationRepository;
    }
}
