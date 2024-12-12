package com.gabriel.donation;

import com.gabriel.donation.entity.User;
import com.gabriel.donation.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Testcontainers
class GabrielDonationCampaignApplicationTests {
	@Autowired
	private UserRepo userRepository;

	@Container
	private static final MySQLContainer<?> mysqlContainer =
			new MySQLContainer<>("mysql:8.0")
					.withDatabaseName("")
					.withUsername("")
					.withPassword("")
					.withReuse(true);

	// Cấu hình datasource cho Spring Boot từ Testcontainers
	// Tạo dữ liệu giả, không ảnh hưởng đến database của dự án (Production)
	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
		registry.add("spring.datasource.username", mysqlContainer::getUsername);
		registry.add("spring.datasource.password", mysqlContainer::getPassword);
	}

	private User createUser() {
		return User.builder()
				.name("gabriel")
				.email("gabriel@gmail.com")
				.phone("phone")
				.password("gabriel")
				.balance(50000)
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
