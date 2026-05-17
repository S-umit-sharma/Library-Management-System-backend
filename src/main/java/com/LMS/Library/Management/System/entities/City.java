    package com.LMS.Library.Management.System.entities;

    import jakarta.persistence.*;
    import lombok.Data;
    import lombok.Getter;
    import lombok.Setter;

    import java.util.List;

    @Entity
    @Table(name = "cities")
    @Setter
    @Getter
    public class City {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;
        @OneToMany(mappedBy = "city")
        private List<User> user;
        @ManyToOne
        @JoinColumn(name="state_id")
        private State state;

    }
