package com.gabriel.donation.service;

import com.gabriel.donation.dto.UserDonatedDTO;

import java.util.List;

public interface UserDonatedService {
    List<UserDonatedDTO> getAll();

    UserDonatedDTO addUserDonated(UserDonatedDTO userDonatedDTO);

    UserDonatedDTO updateUserDonated(UserDonatedDTO userDonatedDTO, int id);

    void deleteUserDonated(int id);
}
