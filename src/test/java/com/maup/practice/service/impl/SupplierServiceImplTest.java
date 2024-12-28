package com.maup.practice.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.maup.practice.model.SupplierModel;
import com.maup.practice.repository.SupplierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class SupplierServiceImplTest {

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    @Test
    void shouldFindAllSuppliersWhenSuppliersExist() {
        SupplierModel supplier1 = new SupplierModel();
        supplier1.setId(1L);
        supplier1.setName("Supplier A");

        SupplierModel supplier2 = new SupplierModel();
        supplier2.setId(2L);
        supplier2.setName("Supplier B");

        when(supplierRepository.findAll()).thenReturn(Arrays.asList(supplier1, supplier2));

        List<SupplierModel> suppliers = supplierService.findAllSuppliers();

        verify(supplierRepository, times(1)).findAll();
        assertEquals(2, suppliers.size());
        assertTrue(suppliers.contains(supplier1));
        assertTrue(suppliers.contains(supplier2));
    }

    @Test
    void shouldFindAllSuppliersWhenNoSuppliersExist() {
        when(supplierRepository.findAll()).thenReturn(Arrays.asList());

        List<SupplierModel> suppliers = supplierService.findAllSuppliers();

        verify(supplierRepository, times(1)).findAll();
        assertNotNull(suppliers);
        assertTrue(suppliers.isEmpty());
    }

    @Test
    void shouldFindSupplierByIdWhenSupplierExists() {
        Long supplierId = 3L;
        SupplierModel supplier = new SupplierModel();
        supplier.setId(supplierId);
        supplier.setName("Supplier C");

        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(supplier));

        SupplierModel foundSupplier = supplierService.findSupplierById(supplierId);

        verify(supplierRepository, times(1)).findById(supplierId);
        assertEquals(supplier, foundSupplier);
    }

    @Test
    void shouldThrowRuntimeExceptionWhenSupplierDoesNotExist() {
        Long supplierId = 4L;

        when(supplierRepository.findById(supplierId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            supplierService.findSupplierById(supplierId);
        });

        assertEquals("Supplier not found", exception.getMessage());
        verify(supplierRepository, times(1)).findById(supplierId);
    }
}
