package com.example.demo.services;

import com.example.demo.annotations.Sout;
import com.example.demo.objects.Person;
import com.example.demo.objects.PersonSetterBuilder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PersonService {

    private final Random random = new Random();

    private static final String[] ALPHABET = new String[]{
            "А", "Б", "В", "Г", "Д", "Е",
            "Ё", "Ж", "З", "И", "Й", "К",
            "Л", "М", "Н", "О", "П", "Р",
            "С", "Т", "У", "Ф", "Х", "Ц",
            "Ч", "Ш", "Щ", "Ъ", "Ы", "Ь",
            "Э", "Ю", "Я"
    };

    @Sout
    public List<Person> getRandomPeople(int count) {
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            people.add(
                    new PersonSetterBuilder()
                            .setName(getRandomName())
                            .setAge(getRandomAge())
                            .build()
            );
        }

        return people.isEmpty() ? Collections.emptyList() : people;
    }

    private String getRandomAge() {
        return String.valueOf(random.nextInt(100));
    }

    private String getRandomName() {
        StringBuilder result = new StringBuilder();
        int resultLength = random.nextInt(10);
        for (int i = 0; i < resultLength + 1; i++) {
            result.append(ALPHABET[random.nextInt(33)]);
        }
        return result.toString();
    }
}
