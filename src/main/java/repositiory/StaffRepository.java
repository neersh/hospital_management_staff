package repositiory;

import controller.dto.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    String findByUsername(String userName);
}
