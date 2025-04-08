package es.dws.gym.gym.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.dws.gym.gym.model.Gimclass;
import es.dws.gym.gym.model.Review;
import es.dws.gym.gym.model.User;
import es.dws.gym.gym.repository.GimclassRepository;
import es.dws.gym.gym.repository.UserRepository;
import jakarta.annotation.PostConstruct;

/**
 * DefaultDataService.java
 *
 * This service class is responsible for initializing default data in the database.
 * It creates test users, associated reviews, and gym classes with enrolled users when the application starts.
 */

@Service
public class DefaultDataService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GimclassRepository gimclassRepository;

    @PostConstruct
    public void init() {
        try {
            // Create a test user
            User prueba = new User("prueba", "Prueba", "Usuario", "+34898764322", "prueba@prueba.com", "Calle Prueba", "prueba", null);

            java.util.Date utilDate = new SimpleDateFormat("dd/MM/yyyy").parse("21/07/2023");
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            prueba.getReviews().addAll(Arrays.asList(
                new Review("Esto es el 1 comentario de prueba", sqlDate),
                new Review("Esto es el 2 comentario de prueba", sqlDate),
                new Review("Esto es el 3 comentario de prueba", sqlDate)
            ));

            prueba.getReviews().forEach(review -> review.setUser(prueba));

            // Create additional users with fictitious names
            User alice = new User("alice123", "Alice", "Wonderland", "+34123456789", "alice@example.com", "123 Main St", "password1", null);
            User bob = new User("bob456", "Bob", "Builder", "+34987654321", "bob@example.com", "456 Elm St", "password2", null);
            User charlie = new User("charlie789", "Charlie", "Chaplin", "+34111222333", "charlie@example.com", "789 Oak St", "password3", null);
            User diana = new User("diana101", "Diana", "Prince", "+34999988877", "diana@example.com", "321 Pine St", "password4", null);
            User edward = new User("edward202", "Edward", "Scissorhands", "+34888877766", "edward@example.com", "654 Maple St", "password5", null);

            alice.getReviews().addAll(Arrays.asList(
                new Review("Great gym, highly recommend!", sqlDate),
                new Review("Clean facilities and friendly staff.", sqlDate)
            ));

            bob.getReviews().addAll(Arrays.asList(
                new Review("The classes are amazing!", sqlDate),
                new Review("Good value for the price.", sqlDate)
            ));

            charlie.getReviews().addAll(Arrays.asList(
                new Review("The equipment is top-notch.", sqlDate),
                new Review("I love the yoga classes.", sqlDate)
            ));

            diana.getReviews().addAll(Arrays.asList(
                new Review("The trainers are very helpful.", sqlDate),
                new Review("The gym is a bit crowded in the evenings.", sqlDate)
            ));

            edward.getReviews().addAll(Arrays.asList(
                new Review("The app makes booking classes easy.", sqlDate),
                new Review("The parking is convenient.", sqlDate)
            ));

            alice.getReviews().forEach(review -> review.setUser(alice));
            bob.getReviews().forEach(review -> review.setUser(bob));
            charlie.getReviews().forEach(review -> review.setUser(charlie));
            diana.getReviews().forEach(review -> review.setUser(diana));
            edward.getReviews().forEach(review -> review.setUser(edward));

            // Save users to the database
            userRepository.saveAll(Arrays.asList(prueba, alice, bob, charlie, diana, edward));

            // Create gym classes
            Gimclass class1 = new Gimclass("Yoga Basics", "A beginner-friendly yoga class.", sqlDate, java.sql.Time.valueOf("10:00:00"));
            Gimclass class2 = new Gimclass("HIIT Training", "High-intensity interval training for all levels.", sqlDate, java.sql.Time.valueOf("18:00:00"));
            Gimclass class3 = new Gimclass("Pilates Core", "Strengthen your core with Pilates.", sqlDate, java.sql.Time.valueOf("15:00:00"));

            // Assign users to classes
            class1.getUsers().addAll(Arrays.asList(prueba, alice, bob, charlie));
            class2.getUsers().addAll(Arrays.asList(charlie, diana));
            class3.getUsers().addAll(Arrays.asList(diana, edward));

            // Save classes to the database
            gimclassRepository.saveAll(Arrays.asList(class1, class2, class3));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
