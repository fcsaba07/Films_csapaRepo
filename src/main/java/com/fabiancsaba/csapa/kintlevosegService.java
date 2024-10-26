package com.fabiancsaba.csapa;

import com.fabiancsaba.csapa.kintlevoseg;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class kintlevosegService {
    private final kintlevosegRepository kintlevosegRepository;

    @Autowired
    public kintlevosegService(kintlevosegRepository kintlevosegRepository) {
        this.kintlevosegRepository = kintlevosegRepository;
    }

    public List<kintlevoseg> getAllKintlevoseg() {
        return kintlevosegRepository.findAll();
    }

    public kintlevoseg saveKintlevoseg(kintlevoseg kintlevoseg) {
        return kintlevosegRepository.save(kintlevoseg);

    }

}