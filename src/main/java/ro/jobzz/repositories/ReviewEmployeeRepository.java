package ro.jobzz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.jobzz.entities.ReviewEmployee;

@Repository
public interface ReviewEmployeeRepository extends JpaRepository<ReviewEmployee, Integer> {

    @SuppressWarnings("unchecked")
    ReviewEmployee saveAndFlush(ReviewEmployee review);

}
