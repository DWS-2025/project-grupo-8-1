package es.dws.gym.gym.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        try {
            // Create a test user
            User prueba = new User("prueba", "Prueba", "Prueba", "+34898764322", "prueba@prueba.com", "Calle Prueba", passwordEncoder.encode("prueba"), null, "USER");

            java.util.Date utilDate = new SimpleDateFormat("dd/MM/yyyy").parse("21/07/2023");
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            prueba.getReviews().addAll(Arrays.asList(
                new Review("<p>Esto es el 1 comentario de prueba</p>", sqlDate),
                new Review("<p>Esto es el 2 comentario de prueba</p>", sqlDate),
                new Review("<p>Esto es el 3 comentario de prueba</p>", sqlDate)
            ));

            prueba.getReviews().forEach(review -> review.setUser(prueba));

            // Create additional users with fictitious names
            User alice = new User("alice123", "Alice", "Wonderland", "+34123456789", "alice@example.com", "123 Main St", passwordEncoder.encode("password1"), null, "USER");
            User bob = new User("bob456", "Bob", "Builder", "+34987654321", "bob@example.com", "456 Elm St", passwordEncoder.encode("password2"), null, "USER");
            User charlie = new User("charlie789", "Charlie", "Chaplin", "+34111222333", "charlie@example.com", "789 Oak St", passwordEncoder.encode("password3"), null, "USER");
            User diana = new User("diana101", "Diana", "Prince", "+34999988877", "diana@example.com", "321 Pine St", passwordEncoder.encode("password4"), null, "USER");
            User admin = new User("admin", "Edward", "Scissorhands", "+34888877766", "edward@example.com", "654 Maple St", passwordEncoder.encode("password5"), null, "ADMIN");

            alice.getReviews().addAll(Arrays.asList(
                new Review("<p>Great gym, highly recommend!</p>", sqlDate),
                new Review("<p>Clean facilities and friendly staff.</p>", sqlDate)
            ));

            bob.getReviews().addAll(Arrays.asList(
                new Review("<p>The classes are amazing!</p>", sqlDate),
                new Review("<p>Good value for the price.</p>", sqlDate)
            ));

            charlie.getReviews().addAll(Arrays.asList(
                new Review("<p>The equipment is top-notch.</p>", sqlDate),
                new Review("<p>I love the yoga classes.</p>", sqlDate)
            ));

            diana.getReviews().addAll(Arrays.asList(
                new Review("<p>The trainers are very helpful.</p>", sqlDate),
                new Review("<p>The gym is a bit crowded in the evenings.</p>", sqlDate)
            ));

            admin.getReviews().addAll(Arrays.asList(
                new Review("<p>The app makes booking classes easy.</p>", sqlDate),
                new Review("<p>The parking is convenient.</p>", sqlDate)
            ));

            alice.getReviews().forEach(review -> review.setUser(alice));
            bob.getReviews().forEach(review -> review.setUser(bob));
            charlie.getReviews().forEach(review -> review.setUser(charlie));
            diana.getReviews().forEach(review -> review.setUser(diana));
            admin.getReviews().forEach(review -> review.setUser(admin));

            // Save users to the database
            userRepository.saveAll(Arrays.asList(prueba, alice, bob, charlie, diana, admin));

            // Create gym classes
            Gimclass class1 = new Gimclass("Yoga Basics", "A beginner-friendly yoga class.", sqlDate, java.sql.Time.valueOf("10:00:00"));
            Gimclass class2 = new Gimclass("HIIT Training", "High-intensity interval training for all levels.", sqlDate, java.sql.Time.valueOf("18:00:00"));
            Gimclass class3 = new Gimclass("Pilates Core", "Strengthen your core with Pilates.", sqlDate, java.sql.Time.valueOf("15:00:00"));

            // Assign users to classes
            class1.getUsers().addAll(Arrays.asList(prueba, alice, bob, charlie));
            class2.getUsers().addAll(Arrays.asList(charlie, diana));
            class3.getUsers().addAll(Arrays.asList(diana, admin));

            // Save classes to the database
            gimclassRepository.saveAll(Arrays.asList(class1, class2, class3));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
