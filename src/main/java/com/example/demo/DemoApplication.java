package com.example.demo;

import com.example.demo.objects.Person;
import com.example.demo.objects.PersonSetterBuilder;
import com.example.demo.services.PersonService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public DemoApplication(PersonService personService) {
		List<Person> people = personService.getRandomPeople(10);
		for (Person person : people) {
			System.out.println(person.getName() + " " + person.getAge());
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		Person person = new PersonSetterBuilder()
				.setName("Ivan")
				.setAge("23")
				.build();

		System.out.println(person.getName());
		System.out.println(person.getAge());
	}

}
