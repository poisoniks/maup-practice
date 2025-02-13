package com.maup.practice.service.impl;

import com.maup.practice.model.SupplierModel;
import com.maup.practice.repository.SupplierRepository;
import com.maup.practice.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public List<SupplierModel> findAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Override
    public SupplierModel findSupplierById(Long id) {
        return supplierRepository.findById(id).orElseThrow(() -> new RuntimeException("Supplier not found"));
    }
}
