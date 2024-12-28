package com.maup.practice.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.maup.practice.model.AddressModel;
import com.maup.practice.model.UserModel;
import com.maup.practice.repository.AddressRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    @Test
    void shouldSaveAddressWhenAddressModelIsValid() {
        AddressModel addressModel = new AddressModel();
        addressModel.setCity("New York");
        addressModel.setStreet("5th Avenue");
        addressModel.setCountry("USA");
        addressModel.setState("NY");

        addressService.saveAddress(addressModel);

        verify(addressRepository, times(1)).save(addressModel);
    }

    @Test
    void shouldFindAddressesWhenUserHasAddresses() {
        UserModel user = new UserModel();
        user.setId(1L);
        user.setEmail("user@example.com");

        AddressModel address1 = new AddressModel();
        address1.setCity("Los Angeles");
        address1.setStreet("Sunset Boulevard");
        address1.setCountry("USA");
        address1.setState("CA");
        address1.setUser(user);

        AddressModel address2 = new AddressModel();
        address2.setCity("Chicago");
        address2.setStreet("Michigan Avenue");
        address2.setCountry("USA");
        address2.setState("IL");
        address2.setUser(user);

        when(addressRepository.findByUser(user)).thenReturn(Arrays.asList(address1, address2));

        List<AddressModel> addresses = addressService.findAddresses(user);

        verify(addressRepository, times(1)).findByUser(user);
        assertEquals(2, addresses.size());
        assertTrue(addresses.contains(address1));
        assertTrue(addresses.contains(address2));
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoAddresses() {
        UserModel user = new UserModel();
        user.setId(2L);
        user.setEmail("empty@example.com");

        when(addressRepository.findByUser(user)).thenReturn(List.of());

        List<AddressModel> addresses = addressService.findAddresses(user);

        verify(addressRepository, times(1)).findByUser(user);
        assertNotNull(addresses);
        assertTrue(addresses.isEmpty());
    }
}
