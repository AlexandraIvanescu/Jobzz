package ro.jobzz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.jobzz.entities.EmployerPosting;

import java.util.Date;
import java.util.List;

@Repository
public interface EmployerPostingRepository extends JpaRepository<EmployerPosting, Integer> {

    @SuppressWarnings("unchecked")
    EmployerPosting saveAndFlush(EmployerPosting posting);

    @Query("SELECT p from EmployerPosting p WHERE p.employer.email = ?1 AND p.status <> 3")
    List<EmployerPosting> findAllEmployerPosts(String email);

    void delete(EmployerPosting posting);

    @Query("SELECT p from EmployerPosting p WHERE p.jobId = ?1 AND p.startDate > current_date")
    List<EmployerPosting> findAllAvailablePosts(Integer jobId);

    @Query("SELECT p from EmployerPosting p WHERE p.jobId = ?1 AND p.name LIKE CONCAT('%', ?2, '%') AND p.startDate >= ?3 AND p.endDate <= ?4 ")
    List<EmployerPosting> findAllAvailablePosts(Integer jobId, String name, Date startDate, Date endDate);

    @Query("SELECT COUNT(p) FROM EmployerPosting p WHERE p.employer.email = ?1 AND p.status = 2")
    Long numberOfPostInDoneStatus(String email);

}
