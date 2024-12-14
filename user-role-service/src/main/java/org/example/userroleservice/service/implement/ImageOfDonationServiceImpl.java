package org.example.userroleservice.service.implement;

import com.gabriel.donation.dto.ImageOfDonationDTO;
import com.gabriel.donation.entity.DonationPost;
import com.gabriel.donation.entity.ImageOfDonation;
import com.gabriel.donation.mapper.ImageOfDonationMapper;
import com.gabriel.donation.repository.DonationPostRepo;
import com.gabriel.donation.repository.ImageOfDonationRepo;
import com.gabriel.donation.service.ImageOfDonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageOfDonationServiceImpl implements ImageOfDonationService {

    @Autowired
    ImageOfDonationRepo imageOfDonationRepo;
    @Autowired
    DonationPostRepo donationPostRepo;

    @Override
    public Page<ImageOfDonationDTO> getAll(PageRequest pageRequest)
    {
        List<ImageOfDonation> imageOfDonations = imageOfDonationRepo.findAll();
        List<ImageOfDonationDTO> imageOfDonationDTOS = imageOfDonations
                .stream()
                .map(ImageOfDonationMapper.INSTANCE::toDto)
                .toList();
        return new PageImpl<ImageOfDonationDTO>(
                imageOfDonationDTOS,
                imageOfDonationRepo.findAll(pageRequest).getPageable(),
                imageOfDonationRepo.findAll(pageRequest).getTotalElements()
        );
    }

    @Override
    public ImageOfDonationDTO addImageOfDonation(ImageOfDonationDTO imageOfDonationDTO)
    {
        ImageOfDonation imageOfDonation = ImageOfDonationMapper.INSTANCE.toEntity(imageOfDonationDTO);
        ImageOfDonation savedImageOfDonation=imageOfDonationRepo.save(imageOfDonation);
        return ImageOfDonationMapper.INSTANCE.toDto(savedImageOfDonation);
    }

    @Override
    public ImageOfDonationDTO updateImageOfDonation(ImageOfDonationDTO imageOfDonationDTO, int id)
    {
        ImageOfDonation imageOfDonation=imageOfDonationRepo.findById(id).get();
        imageOfDonation.setDescription(imageOfDonationDTO.getDescription());
        imageOfDonation.setUrl(imageOfDonationDTO.getUrl());
        DonationPost donatepost1=donationPostRepo.findById(imageOfDonationDTO.getDonationPostId()).get();
        imageOfDonation.setDonationPost(donatepost1);
        imageOfDonation.setDeleted(imageOfDonationDTO.isDeleted());
        ImageOfDonation updated=imageOfDonationRepo.save(imageOfDonation);
        return ImageOfDonationMapper.INSTANCE.toDto(updated);
    }

    @Override
    public void deleteImageOfDonation(int id)
    {
        if(imageOfDonationRepo.existsById(id))
            imageOfDonationRepo.deleteById(id);
    }

    @Override
    public ImageOfDonationDTO getImageOfDonationById(int id)
    {
        return ImageOfDonationMapper.INSTANCE.toDto(imageOfDonationRepo.findByDonationPostId(id).get(0));
    }

}
