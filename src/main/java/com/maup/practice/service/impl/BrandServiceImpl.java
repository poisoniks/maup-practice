package com.maup.practice.service.impl;

import com.maup.practice.model.BrandModel;
import com.maup.practice.repository.BrandRepository;
import com.maup.practice.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public List<BrandModel> findAllBrands() {
        return brandRepository.findAll();
    }
}
