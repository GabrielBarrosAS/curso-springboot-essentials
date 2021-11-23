package lead.mentoring.springboot2.repository;

import lead.mentoring.springboot2.domain.SpringbootEssentialsUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  SpringbootEssentialsUserRepository extends JpaRepository<SpringbootEssentialsUser,Long> {

    SpringbootEssentialsUser findByUsername(String username);

}
