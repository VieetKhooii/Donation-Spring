package com.gabriel.donation.service;

import com.gabriel.donation.dto.SponsorDTO;

import java.util.List;

public interface SponsorService {
    List<SponsorDTO> getAll();

    SponsorDTO addSponsor(SponsorDTO sponsorDTO);

    SponsorDTO updateSponsor(SponsorDTO sponsorDTO, int id);

    void deleteSponsor(int id);
}
