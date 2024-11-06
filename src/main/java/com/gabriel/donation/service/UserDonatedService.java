package com.gabriel.donation.service;

import com.gabriel.donation.dto.UserDonatedDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface UserDonatedService {
    Page<UserDonatedDTO> getAll(PageRequest pageRequest);

    Page<UserDonatedDTO> getPageByPostId(PageRequest pageRequest, int postId);

    UserDonatedDTO addUserDonated(UserDonatedDTO userDonatedDTO);

    UserDonatedDTO updateUserDonated(UserDonatedDTO userDonatedDTO, int id);

    void deleteUserDonated(int id);

    UserDonatedDTO getUserDonatedById(int id);

    public void processDonation(UserDonatedDTO userDonatedDTO, int donatePersonId);

    List<UserDonatedDTO> rankingUserDonatedByAmount();

    List<UserDonatedDTO> rankingUserDonatedByMonth(int presentYear);

    List<UserDonatedDTO> rankingDonationPostAmountByMonth(int presentYear);

    List<UserDonatedDTO> countUserDonatedByPost(int presentYear);
}
