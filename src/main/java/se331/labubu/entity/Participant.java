package se331.labubu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    Long id;

    String name;
    String telNo;

    @ManyToMany(mappedBy = "participants")
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    List<Event> events = new ArrayList<>();
}