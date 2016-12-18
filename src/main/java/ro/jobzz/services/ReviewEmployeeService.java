package ro.jobzz.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ro.jobzz.entities.Employee;
import ro.jobzz.entities.Employer;
import ro.jobzz.entities.ReviewEmployee;
import ro.jobzz.repositories.EmployeeRepository;
import ro.jobzz.repositories.EmployerRepository;
import ro.jobzz.repositories.ReviewEmployeeRepository;
import ro.jobzz.security.SecurityUtils;

import java.util.Date;

@Service
public class ReviewEmployeeService {

    private ReviewEmployeeRepository repository;
    private EmployerRepository employerRepository;
    private EmployeeRepository employeeRepository;

    @Autowired
    public ReviewEmployeeService(ReviewEmployeeRepository repository, EmployerRepository employerRepository, EmployeeRepository employeeRepository) {
        Assert.notNull(repository, "Review Employee Repository must not be null !");

        this.repository = repository;
        this.employerRepository = employerRepository;
        this.employeeRepository = employeeRepository;
    }

    public boolean saveReview(ReviewEmployee review) {

        try {
            Employer employer = employerRepository.findByEmail(SecurityUtils.getCurrentLogin());
            Date currentDate = new Date();
            Integer employeeNewReputation = calculateEmployeeReputation(review.getEmployee(), review.getPoint());

            employeeRepository.updateReputation(review.getEmployee().getEmployeeId(), employeeNewReputation);

            review.setDate(currentDate);
            review.setEmployerId(employer.getEmployerId());

            repository.saveAndFlush(review);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private Integer calculateEmployeeReputation(Employee employee, Integer points) {
        Integer reputation = employee.getReputation() + points - 5;

        return reputation > 0 ? reputation : 0;
    }

}
