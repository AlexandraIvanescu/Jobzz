package ro.jobzz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.jobzz.entities.ReviewEmployer;

@Repository
public interface ReviewEmployeeRepository extends JpaRepository<ReviewEmployer, Integer> {
}
