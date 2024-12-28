package com.maup.practice.facade.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.maup.practice.dto.AddressDTO;
import com.maup.practice.model.AddressModel;
import com.maup.practice.model.UserModel;
import com.maup.practice.service.AddressService;
import com.maup.practice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AddressFacadeImplTest {

    @Mock
    private AddressService addressService;

    @Mock
    private UserService userService;

    @Mock
    private Converter<AddressModel, AddressDTO> addressConverter;

    @InjectMocks
    private AddressFacadeImpl addressFacade;

    @Test
    void shouldAddAddressWhenValidAddressDTO() {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setCity("New York");
        addressDTO.setStreet("5th Avenue");
        addressDTO.setCountry("USA");
        addressDTO.setState("NY");

        UserModel currentUser = new UserModel();
        currentUser.setId(1L);
        currentUser.setEmail("user@example.com");

        when(userService.getCurrentUser()).thenReturn(currentUser);

        addressFacade.addAddress(addressDTO);

        verify(addressService, times(1)).saveAddress(argThat(addressModel ->
                addressModel.getCity().equals("New York") &&
                addressModel.getStreet().equals("5th Avenue") &&
                addressModel.getCountry().equals("USA") &&
                addressModel.getState().equals("NY") &&
                addressModel.getUser().equals(currentUser)));
    }

    @Test
    void shouldGetAddressesWhenCurrentUserHasAddresses() {
        UserModel currentUser = new UserModel();
        currentUser.setId(2L);
        currentUser.setEmail("test@example.com");

        AddressModel address1 = new AddressModel();
        address1.setCity("Los Angeles");
        address1.setStreet("Sunset Boulevard");
        address1.setCountry("USA");
        address1.setState("CA");
        address1.setUser(currentUser);

        AddressModel address2 = new AddressModel();
        address2.setCity("Chicago");
        address2.setStreet("Michigan Avenue");
        address2.setCountry("USA");
        address2.setState("IL");
        address2.setUser(currentUser);

        AddressDTO addressDTO1 = new AddressDTO();
        addressDTO1.setCity("Los Angeles");
        addressDTO1.setStreet("Sunset Boulevard");
        addressDTO1.setCountry("USA");
        addressDTO1.setState("CA");

        AddressDTO addressDTO2 = new AddressDTO();
        addressDTO2.setCity("Chicago");
        addressDTO2.setStreet("Michigan Avenue");
        addressDTO2.setCountry("USA");
        addressDTO2.setState("IL");

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(addressService.findAddresses(currentUser)).thenReturn(Arrays.asList(address1, address2));
        when(addressConverter.convert(address1)).thenReturn(addressDTO1);
        when(addressConverter.convert(address2)).thenReturn(addressDTO2);

        List<AddressDTO> addresses = addressFacade.getAddresses();

        verify(addressService, times(1)).findAddresses(currentUser);
        verify(addressConverter, times(1)).convert(address1);
        verify(addressConverter, times(1)).convert(address2);
        assertEquals(2, addresses.size());
        assertTrue(addresses.contains(addressDTO1));
        assertTrue(addresses.contains(addressDTO2));
    }

    @Test
    void shouldGetEmptyListWhenCurrentUserHasNoAddresses() {
        UserModel currentUser = new UserModel();
        currentUser.setId(3L);
        currentUser.setEmail("empty@example.com");

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(addressService.findAddresses(currentUser)).thenReturn(Collections.emptyList());

        List<AddressDTO> addresses = addressFacade.getAddresses();

        verify(addressService, times(1)).findAddresses(currentUser);
        verifyNoInteractions(addressConverter);
        assertNotNull(addresses);
        assertTrue(addresses.isEmpty());
    }
}
