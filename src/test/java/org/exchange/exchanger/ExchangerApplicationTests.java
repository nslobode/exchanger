package org.exchange.exchanger;

import org.exchange.exchanger.entities.User;
import org.exchange.exchanger.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@SpringBootTest
class ExchangerApplicationTests {

	@Autowired
	UserRepository userRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void testRepo() {
		User testUser = new User();
		testUser.setName("a user");
		userRepository.save(testUser);
		String id = testUser.getId();
		User referenceById = userRepository.getReferenceById(id);
		System.out.println(id);
		List<User> all = userRepository.findAll();
		all.forEach(System.out::println);

		System.out.println("All name " + all.get(0).getName());
		User fail = userRepository.findById(id).orElse(new User());
		User byId = userRepository.getById(id);
		System.out.println("--fail" + fail);
		System.out.println(referenceById.getId());
	}

}
