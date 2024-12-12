package com.gabriel.donation;

import com.gabriel.donation.entity.User;
import com.gabriel.donation.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepo userRepository;

    // Remember to add @Table(name = "\"user\"") in User entity
    private User createUser() {
        return User.builder()
                .name("gabriel")
                .email("gabriel@gmail.com")
                .phone("phone")
                .password("gabriel")
                .balance(100000)
                .build();
    }

    @Test
    void testSaveAndFindUser() {
        //Arrange
        User user = createUser();

        //Act
        User savedUser = userRepository.save(user);

        //Assert
//		assertThat(savedUser.getId()).isNotNull();
        User foundUser = userRepository.findById(user.getId());
        assertThat(foundUser.getName()).isEqualTo("gabriel");
        assertThat(foundUser.getEmail()).isEqualTo("gabriel@gmail.com");
    }
}
