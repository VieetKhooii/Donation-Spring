package com.gabriel.donation.service.implement;

import com.gabriel.donation.dto.SponsorDTO;
import com.gabriel.donation.entity.Sponsor;
import com.gabriel.donation.mapper.SponsorMapper;
import com.gabriel.donation.repository.SponsorRepo;
import com.gabriel.donation.service.SponsorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SponsorServiceImpl implements SponsorService {

    @Autowired
    SponsorRepo sponsorRepo;

    @Override
    public Page<SponsorDTO> getAll(PageRequest pageRequest)
    {
        List<Sponsor> sponsors = sponsorRepo.findAll(pageRequest).getContent();
        List<SponsorDTO> sponsorDTOS = sponsors
                .stream()
                .map(SponsorMapper.INSTANCE::toDto)
                .toList();
        return new PageImpl<SponsorDTO>(
                sponsorDTOS,
                sponsorRepo.findAll(pageRequest).getPageable(),
                sponsorRepo.findAll(pageRequest).getTotalElements()
        );
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

    @Override
    public SponsorDTO getSponsorById(int id)
    {
        return SponsorMapper.INSTANCE.toDto(sponsorRepo.findById(id).get());
    }

}
