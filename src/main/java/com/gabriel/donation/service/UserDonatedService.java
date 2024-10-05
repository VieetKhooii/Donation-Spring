package com.gabriel.donation.service;

import com.gabriel.donation.dto.UserDonatedDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface UserDonatedService {
    Page<UserDonatedDTO> getAll(PageRequest pageRequest);

    UserDonatedDTO addUserDonated(UserDonatedDTO userDonatedDTO);

    UserDonatedDTO updateUserDonated(UserDonatedDTO userDonatedDTO, int id);

    void deleteUserDonated(int id);

    UserDonatedDTO getUserDonatedById(int id);

    public void processDonation(UserDonatedDTO userDonatedDTO, int donatePersonId);
}
