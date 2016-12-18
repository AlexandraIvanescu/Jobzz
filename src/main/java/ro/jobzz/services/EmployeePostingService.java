package ro.jobzz.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ro.jobzz.constants.EmployeePostStatus;
import ro.jobzz.entities.Employee;
import ro.jobzz.entities.EmployeePosting;
import ro.jobzz.entities.Employer;
import ro.jobzz.entities.EmployerPosting;
import ro.jobzz.repositories.EmployeePostingRepository;
import ro.jobzz.repositories.EmployeeRepository;
import ro.jobzz.security.SecurityUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EmployeePostingService {

    private EmployeePostingRepository postingRepository;
    private EmployeeRepository employeeRepository;


    @Autowired
    public EmployeePostingService(EmployeePostingRepository postingRepository, EmployeeRepository employeeRepository) {
        Assert.notNull(postingRepository, "Employee Posting Repository must not be null !");

        this.postingRepository = postingRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeePosting> getAllEmployeePost() {
        List<EmployeePosting> postings = postingRepository.findAllEmployeePosts(SecurityUtils.getCurrentLogin());
        List<EmployeePosting> deletePostings = new ArrayList<>();
        Date currentDate = new Date();

        postings.forEach(post -> {

            if (post.getStatus() == 0 && post.getEmployerPosting().getStartDate().before(currentDate)) {
                deletePostings.add(post);
            } else if (post.getStatus() == 1 && post.getEmployerPosting().getStartDate().before(currentDate)) {
                post.setStatus(3);
                postingRepository.saveAndFlush(post);
            } else if (post.getStatus() == 2 && post.getEmployerPosting().getStartDate().before(currentDate)) {
                deletePostings.add(post);
            }

            if (post.getStatus() == 3 && post.getEmployerPosting().getEndDate().before(currentDate)) {
                post.setStatus(5);
                postingRepository.saveAndFlush(post);
            }

        });

        deletePostings.forEach(post -> {
            postings.remove(post);
            postingRepository.delete(post);
        });

        postings.forEach(pos -> {
            pos.setEmployee(null);
            hiddenEmployerPosting(pos);
        });

        return postings;
    }

    public boolean createPost(EmployeePosting post) {

        try {
            Employee employee = employeeRepository.findByEmail(SecurityUtils.getCurrentLogin());
            Date currentDate = new Date();

            post.setStatus(EmployeePostStatus.WAITING_NO_RESPONSE.getStatus());
            post.setEmployee(employee);
            post.setDate(currentDate);

            postingRepository.saveAndFlush(post);
        } catch (Exception e) {
            return false;
        }

        return true;

    }

    public boolean updateStatus(EmployeePosting post) {

        try {

            postingRepository.updateStatus(post.getEmployeePostingId(), post.getStatus());

        } catch (Exception e) {
            return false;
        }

        return true;
    }


    private void hiddenEmployerPosting(EmployeePosting posting) {
        EmployerPosting employerPosting = posting.getEmployerPosting();
        EmployerPosting hiddenEmployerPosting = new EmployerPosting();

        // Hidden Some Employer Details
        Employer hiddenEmployer = new Employer();
        Employer employer = employerPosting.getEmployer();

        hiddenEmployer.setEmail(employer.getEmail());
        hiddenEmployer.setPhoneNumber(employer.getPhoneNumber());
        hiddenEmployer.setDateOfBirth(employer.getDateOfBirth());
        hiddenEmployer.setFirstName(employer.getFirstName());
        hiddenEmployer.setLastName(employer.getLastName());
        hiddenEmployer.setReputation(employer.getReputation());
        hiddenEmployer.setProfilePicture(employer.getProfilePicture());
        hiddenEmployer.setReviewEmployer(employer.getReviewEmployer());

        hiddenEmployerPosting.setEmployer(hiddenEmployer);

        // Hidden Some Employer Posting Detail
        hiddenEmployerPosting.setEmployerPostingId(employerPosting.getEmployerPostingId());
        hiddenEmployerPosting.setName(employerPosting.getName());
        hiddenEmployerPosting.setDescription(employerPosting.getDescription());
        hiddenEmployerPosting.setLongitude(employerPosting.getLongitude());
        hiddenEmployerPosting.setLatitude(employerPosting.getLatitude());
        hiddenEmployerPosting.setJobId(employerPosting.getJobId());
        hiddenEmployerPosting.setStartDate(employerPosting.getStartDate());
        hiddenEmployerPosting.setEndDate(employerPosting.getEndDate());
        hiddenEmployerPosting.setStatus(employerPosting.getStatus());

        posting.setEmployerPosting(hiddenEmployerPosting);

    }

}
