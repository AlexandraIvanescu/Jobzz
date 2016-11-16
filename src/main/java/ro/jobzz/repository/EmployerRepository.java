package ro.jobzz.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.jobzz.entity.Employer;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Integer> {
}
