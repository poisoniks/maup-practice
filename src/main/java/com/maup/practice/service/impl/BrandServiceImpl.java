package com.maup.practice.service.impl;

import com.maup.practice.model.BrandModel;
import com.maup.practice.repository.BrandRepository;
import com.maup.practice.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    @Autowired
    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<BrandModel> findAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public BrandModel findBrandById(Long id) {
        return brandRepository.findById(id).orElseThrow(() -> new RuntimeException("Brand not found"));
    }
}
