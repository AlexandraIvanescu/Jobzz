package ro.jobzz.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ro.jobzz.entities.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @SuppressWarnings("unchecked")
    Employee saveAndFlush(Employee employee);

    Employee findByEmail(String email);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Employee e SET e.reputation =:reputation WHERE e.employeeId =:employeeId")
    void updateReputation(@Param("employeeId") Integer employeeId, @Param("reputation") Integer reputation);

}
