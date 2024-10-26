package com.fabiancsaba.csapa;

import com.fabiancsaba.csapa.kintlevoseg;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<Long> getAllFilmIdsInKintlevoseg() {
        List<kintlevoseg> kintlevosegek = getAllKintlevoseg();
        return kintlevosegek.stream()
                .map(k -> k.getFilmId())
                .collect(Collectors.toList());
    }
    public kintlevoseg deleteKintlevoseg(kintlevoseg kintlevoseg) {
        kintlevosegRepository.delete(kintlevoseg);
        return kintlevoseg;
    }
}