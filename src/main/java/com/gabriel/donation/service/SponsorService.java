package com.gabriel.donation.service;

import com.gabriel.donation.dto.SponsorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface SponsorService {
    Page<SponsorDTO> getAll(PageRequest pageRequest);

    SponsorDTO addSponsor(SponsorDTO sponsorDTO);

    SponsorDTO updateSponsor(SponsorDTO sponsorDTO, int id);

    void deleteSponsor(int id);

    SponsorDTO getSponsorById(int id);
}
