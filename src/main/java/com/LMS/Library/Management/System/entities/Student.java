import com.LMS.Library.Management.System.entities.Membership;
import com.LMS.Library.Management.System.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "students")
@Getter
@Setter
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studentId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String collegeName;

    private String course;

    private Integer semester;

    private String enrollmentNumber;

    @OneToMany(mappedBy = "student")
    private List<Membership> memberships;
}