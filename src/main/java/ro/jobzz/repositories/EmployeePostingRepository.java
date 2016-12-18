package ro.jobzz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.jobzz.entities.EmployeePosting;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface EmployeePostingRepository extends JpaRepository<EmployeePosting, Integer> {

    @SuppressWarnings("unchecked")
    EmployeePosting saveAndFlush(EmployeePosting posting);

    @Query("SELECT p FROM EmployeePosting p WHERE p.employee.email = ?1 AND p.status <> 7")
    List<EmployeePosting> findAllEmployeePosts(String email);

    void delete(EmployeePosting posting);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE EmployeePosting p SET p.status =:status WHERE p.employeePostingId =:employeePostingId")
    void updateStatus(@Param("employeePostingId") Integer employeePostingId, @Param("status") Integer status);

}
