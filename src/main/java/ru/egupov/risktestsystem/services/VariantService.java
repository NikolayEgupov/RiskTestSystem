package ru.egupov.risktestsystem.services;

import org.springframework.stereotype.Service;
import ru.egupov.risktestsystem.models.Variant;
import ru.egupov.risktestsystem.repositories.VariantRepository;

@Service
public class VariantService {

    private final VariantRepository variantRepository;

    public VariantService(VariantRepository variantRepository) {
        this.variantRepository = variantRepository;
    }

    public Variant findById(int id){
        return variantRepository.findById(id).orElse(null);
    }

    public void save(Variant variant){
        variantRepository.save(variant);
    }

    public void update(int id, Variant variant){
        variant.setId(id);
        save(variant);
    }

    public void delete(int id){
        variantRepository.deleteById(id);
    }
}
