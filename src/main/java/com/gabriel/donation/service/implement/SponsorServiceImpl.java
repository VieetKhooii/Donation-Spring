package com.gabriel.donation.service.implement;

import com.gabriel.donation.dto.SponsorDTO;
import com.gabriel.donation.entity.Sponsor;
import com.gabriel.donation.mapper.SponsorMapper;
import com.gabriel.donation.repository.SponsorRepo;
import com.gabriel.donation.service.SponsorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SponsorServiceImpl implements SponsorService {

    @Autowired
    SponsorRepo sponsorRepo;

    @Override
    public List<SponsorDTO> getAll()
    {
        List<Sponsor> sponsors = sponsorRepo.findAll();
        return  sponsors.stream()
                .map(SponsorMapper.INSTANCE::toDto)
                .toList();
    }

    @Override
    public SponsorDTO addSponsor(SponsorDTO sponsorDTO)
    {
        Sponsor sponsor=SponsorMapper.INSTANCE.toEntity(sponsorDTO);
        Sponsor savedSponsor=sponsorRepo.save(sponsor);
        return SponsorMapper.INSTANCE.toDto(savedSponsor);
    }

    @Override
    public SponsorDTO updateSponsor(SponsorDTO sponsorDTO, int id)
    {
        Sponsor sponsor=sponsorRepo.findById(id).get();
        sponsor.setName(sponsorDTO.getName());
        sponsor.setAvatar(sponsorDTO.getAvatar());
        sponsor.setDeleted(sponsorDTO.isDeleted());
        sponsor.setDescription(sponsorDTO.getDescription());
        Sponsor updatedSponsor=sponsorRepo.save(sponsor);
        return SponsorMapper.INSTANCE.toDto(updatedSponsor);
    }

    @Override
    public void deleteSponsor(int id)
    {
        if(sponsorRepo.existsById(id))
            sponsorRepo.deleteById(id);
    }

}
