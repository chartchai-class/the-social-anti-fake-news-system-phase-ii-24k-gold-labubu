package se331.labubu.config;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
//import se331.lab.entity.*;
import se331.labubu.repository.EventRepository;
import se331.labubu.repository.ParticipantRepository;
//import se331.lab.entity.Role;
//import se331.lab.repository.UserRepository;
//import se331.lab.entity.User;  // ✅ Correct

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitApp implements ApplicationListener<ApplicationReadyEvent> {

//    final EventRepository eventRepository;
//    final OrganizerRepository organizerRepository;
//    final ParticipantRepository participantRepository;
//    final UserRepository userRepository;
//    final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {

        // ลบข้อมูลเก่าทั้งหมด
//        eventRepository.deleteAll();
//        organizerRepository.deleteAll();
//        participantRepository.deleteAll();
//        userRepository.deleteAll();

        // Add Users
//        addUser();

        // 1. สร้าง Organizers
        Organizer org1 = organizerRepository.save(Organizer.builder()
                .name("CMU Events")
                .build());

        Organizer org2 = organizerRepository.save(Organizer.builder()
                .name("Student Union")
                .build());

        // 2. สร้าง Participants 5 คน
        Participant p1 = participantRepository.save(Participant.builder()
                .name("John Doe")
                .telNo("081-111-1111")
                .build());

        Participant p2 = participantRepository.save(Participant.builder()
                .name("Jane Smith")
                .telNo("081-222-2222")
                .build());

        Participant p3 = participantRepository.save(Participant.builder()
                .name("Bob Johnson")
                .telNo("081-333-3333")
                .build());

        Participant p4 = participantRepository.save(Participant.builder()
                .name("Alice Brown")
                .telNo("081-444-4444")
                .build());

        Participant p5 = participantRepository.save(Participant.builder()
                .name("Charlie Wilson")
                .telNo("081-555-5555")
                .build());

        // 3. สร้าง Events พร้อม Organizer และ Participants
        Event event1 = Event.builder()
                .title("Midterm Exam")
                .category("Academic")
                .description("A time for taking the exam")
                .location("CMU")
                .date("2025-11-15")
                .time("09:00")
                .petAllowed(false)
                .organizer(org1)
                .participants(new ArrayList<>(List.of(p1, p2, p3)))
                .build();
        eventRepository.save(event1);

        Event event2 = Event.builder()
                .title("Commencement Day")
                .category("Ceremony")
                .description("A time for Celebration")
                .location("CMU Stadium")
                .date("2025-12-20")
                .time("10:00")
                .petAllowed(false)
                .organizer(org2)
                .participants(new ArrayList<>(List.of(p1, p2, p3, p4)))
                .build();
        eventRepository.save(event2);

        Event event3 = Event.builder()
                .title("Loy Krathong")
                .category("Festival")
                .description("A time for Krathong")
                .location("Ping River")
                .date("2025-11-25")
                .time("18:00")
                .petAllowed(true)
                .organizer(org1)
                .participants(new ArrayList<>(List.of(p1, p2, p3, p5)))
                .build();
        eventRepository.save(event3);

        Event event4 = Event.builder()
                .title("Music Festival")
                .category("Entertainment")
                .description("Live music performances")
                .location("CMU Arts Center")
                .date("2025-12-01")
                .time("19:00")
                .petAllowed(false)
                .organizer(org2)
                .participants(new ArrayList<>(List.of(p1, p2, p4, p5)))
                .build();
        eventRepository.save(event4);

        Event event5 = Event.builder()
                .title("Sports Day")
                .category("Sports")
                .description("Annual sports competition")
                .location("CMU Sports Complex")
                .date("2025-12-10")
                .time("08:00")
                .petAllowed(false)
                .organizer(org1)
                .participants(new ArrayList<>(List.of(p3, p4, p5)))
                .build();
        eventRepository.save(event5);

        System.out.println("✅ Data initialized successfully!");
        System.out.println("📊 Created " + organizerRepository.count() + " organizers");
        System.out.println("📊 Created " + participantRepository.count() + " participants");
        System.out.println("📊 Created " + eventRepository.count() + " events");
//        System.out.println("📊 Created " + userRepository.count() + " users");
    }

//    private void addUser() {
//        User user1 = User.builder()
//                .username("admin")
//                .password(passwordEncoder.encode("admin"))
//                .firstname("admin")
//                .lastname("admin")
//                .email("admin@admin.com")
//                .enabled(true)
//                .build();
//
//        User user2 = User.builder()
//                .username("user")
//                .password(passwordEncoder.encode("user"))
//                .firstname("user")
//                .lastname("user")
//                .email("enabled@user.com")
//                .enabled(true)
//                .build();
//
//        User user3 = User.builder()
//                .username("disableUser")
//                .password(passwordEncoder.encode("disableUser"))
//                .firstname("disableUser")
//                .lastname("disableUser")
//                .email("disableUser@user.com")
//                .enabled(false)
//                .build();
//
//        user1.getRoles().add(Role.ROLE_USER);
//        user1.getRoles().add(Role.ROLE_ADMIN);
//
//        user2.getRoles().add(Role.ROLE_USER);
//        user3.getRoles().add(Role.ROLE_USER);
//
//        userRepository.save(user1);
//        userRepository.save(user2);
//        userRepository.save(user3);
//    }
}