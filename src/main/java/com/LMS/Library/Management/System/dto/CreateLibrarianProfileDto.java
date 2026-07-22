import com.LMS.Library.Management.System.entities.Library;
import com.LMS.Library.Management.System.entities.User;
import com.LMS.Library.Management.System.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateLibrarianProfileDto {

    private String qualification;

    private Double totalExperienceYears;

    private String specialization;

    private String certifications;

    private String preferredDesignation;

    private String bio;

}