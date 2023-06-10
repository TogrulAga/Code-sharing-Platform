package platform;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CodeRepository extends JpaRepository<Code, UUID> {
    @Query(value = "SELECT * FROM code WHERE expires_by_time = FALSE AND expires_by_views = FALSE ORDER BY long_id DESC LIMIT 10", nativeQuery = true)
    List<Code> findTop10DoesNotExpire();
}
