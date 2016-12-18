package ro.jobzz.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ro.jobzz.constants.EmployeePostStatus;
import ro.jobzz.constants.EmployerPostStatus;
import ro.jobzz.entities.Employee;
import ro.jobzz.entities.EmployeePosting;
import ro.jobzz.entities.Employer;
import ro.jobzz.entities.EmployerPosting;
import ro.jobzz.repositories.EmployeePostingRepository;
import ro.jobzz.repositories.EmployeeRepository;
import ro.jobzz.repositories.EmployerPostingRepository;
import ro.jobzz.repositories.EmployerRepository;
import ro.jobzz.security.SecurityUtils;

import java.util.*;

@Service
public class EmployerPostingService {

    private EmployerPostingRepository postingRepository;
    private EmployeePostingRepository employeePostingRepository;
    private EmployerRepository employerRepository;
    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployerPostingService(EmployerPostingRepository postingRepository, EmployerRepository employerRepository,
                                  EmployeeRepository employeeRepository, EmployeePostingRepository employeePostingRepository) {
        Assert.notNull(postingRepository, "Employer Posting Repository must not be null !");

        this.postingRepository = postingRepository;
        this.employerRepository = employerRepository;
        this.employeeRepository = employeeRepository;
        this.employeePostingRepository = employeePostingRepository;
    }

    public boolean createNewPost(EmployerPosting posting) {

        try {
            Employer employer = employerRepository.findByEmail(SecurityUtils.getCurrentLogin());

            posting.setStatus(EmployerPostStatus.WAITING.getStatus());
            posting.setEmployer(employer);

            postingRepository.saveAndFlush(posting);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public boolean updatePost(EmployerPosting posting) {
        try {
            Employer employer = employerRepository.findByEmail(SecurityUtils.getCurrentLogin());
            posting.setEmployer(employer);

            postingRepository.saveAndFlush(posting);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public boolean deletePost(EmployerPosting posting) {
        try {
            postingRepository.delete(posting);

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public List<EmployerPosting> findAllEmployerPosts() {
        List<EmployerPosting> postings = postingRepository.findAllEmployerPosts(SecurityUtils.getCurrentLogin());
        List<EmployerPosting> closePostings = new ArrayList<>();
        Date currentDate = new Date();

        if (postings == null) {
            return new ArrayList<>();
        }

        postings.forEach(posting -> {

            if (posting.getEndDate().after(currentDate) && posting.getStartDate().before(currentDate) && posting.getStatus() < 1) {
                long validEmployeePost = posting.getEmployeePostings().stream()
                        .filter(post -> post.getStatus().equals(EmployeePostStatus.WAITING_ACCEPTED.getStatus())).count();

                if (validEmployeePost > 0) {
                    posting.setStatus(EmployerPostStatus.IN_PROGRESS.getStatus());
                    postingRepository.saveAndFlush(posting);

                    posting.getEmployeePostings().forEach(post -> {
                        if (post.getStatus().equals(EmployeePostStatus.WAITING_ACCEPTED.getStatus())) {
                            post.setStatus(EmployeePostStatus.IN_PROGRESS.getStatus());
                        } else {
                            post.setStatus(EmployeePostStatus.CLOSED.getStatus());
                        }

                        employeePostingRepository.saveAndFlush(post);
                    });

                } else {
                    closePostings.add(posting);
                }

            } else if (posting.getEndDate().before(currentDate) && posting.getStatus() < 2) {
                long validEmployeePost = posting.getEmployeePostings().stream()
                        .filter(post -> post.getStatus().equals(EmployeePostStatus.IN_PROGRESS.getStatus())).count();

                if (validEmployeePost > 0) {
                    posting.setStatus(EmployerPostStatus.DONE.getStatus());
                    postingRepository.saveAndFlush(posting);

                    posting.getEmployeePostings().forEach(post -> {

                        if (post.getStatus().equals(EmployeePostStatus.IN_PROGRESS.getStatus())) {
                            post.setStatus(EmployeePostStatus.DONE_WAITING.getStatus());
                            employeePostingRepository.saveAndFlush(post);
                        }

                    });

                } else {
                    closePostings.add(posting);
                }

            }

        });

        closePostings.forEach(posting -> {
            posting.setStatus(EmployerPostStatus.CLOSED.getStatus());
            postingRepository.saveAndFlush(posting);
            postings.remove(posting);
        });

        postings.forEach(posting -> {
            posting.setEmployer(null);

            if (posting.getEmployeePostings() != null && posting.getEmployeePostings().size() > 0) {
                hiddenEmployeePostingDetails(posting);
            }

        });

        return postings;
    }

    public List<EmployerPosting> findAllAvailablePostsForEmployee() {
        Employee employee = employeeRepository.findByEmail(SecurityUtils.getCurrentLogin());

        if (employee == null) {
            return null;
        }

        List<EmployerPosting> postings = postingRepository.findAllAvailablePosts(employee.getJob().getJobId());

        return filterPostsForEmployee(postings, employee);
    }

    public List<EmployerPosting> findAllAvailablePostsForEmployee(String name, Date startDate, Date endDate) {
        Employee employee = employeeRepository.findByEmail(SecurityUtils.getCurrentLogin());

        if (employee == null) {
            return null;
        }

        List<EmployerPosting> postings = postingRepository.findAllAvailablePosts(employee.getJob().getJobId(), name, startDate, endDate);

        return filterPostsForEmployee(postings, employee);
    }

    public boolean allowNewPost() {
        Employer employer = employerRepository.findByEmail(SecurityUtils.getCurrentLogin());

        return postingRepository.numberOfPostInDoneStatus(employer.getEmail()) == 0;
    }

    private void hiddenEmployeePostingDetails(EmployerPosting posting) {
        Set<EmployeePosting> employeePostings = posting.getEmployeePostings();
        Set<EmployeePosting> hiddenEmployeePostings = new HashSet<>();

        for (EmployeePosting employeePosting : employeePostings) {

            if (employeePosting.getStatus() == 4 || employeePosting.getStatus() == 6 || employeePosting.getStatus() == 7) {
                continue;
            }

            EmployeePosting hiddenEmployeePosting = new EmployeePosting();

            // Hidden Employee Details
            Employee hiddenEmployee = new Employee();
            Employee employee = employeePosting.getEmployee();

            hiddenEmployee.setEmployeeId(employee.getEmployeeId());
            hiddenEmployee.setEmail(employee.getEmail());
            hiddenEmployee.setPhoneNumber(employee.getPhoneNumber());
            hiddenEmployee.setDateOfBirth(employee.getDateOfBirth());
            hiddenEmployee.setFirstName(employee.getFirstName());
            hiddenEmployee.setLastName(employee.getLastName());
            hiddenEmployee.setReputation(employee.getReputation());
            hiddenEmployee.setPicture(employee.getPicture());

            hiddenEmployeePosting.setEmployee(hiddenEmployee);

            // Hidden Employee Posting Details
            hiddenEmployeePosting.setEmployeePostingId(employeePosting.getEmployeePostingId());
            hiddenEmployeePosting.setPrice(employeePosting.getPrice());
            hiddenEmployeePosting.setCurrency(employeePosting.getCurrency());
            hiddenEmployeePosting.setDate(employeePosting.getDate());
            hiddenEmployeePosting.setComment(employeePosting.getComment());
            hiddenEmployeePosting.setStatus(employeePosting.getStatus());

            hiddenEmployeePostings.add(hiddenEmployeePosting);
        }

        posting.setEmployeePostings(hiddenEmployeePostings);
    }

    private List<EmployerPosting> filterPostsForEmployee(List<EmployerPosting> postings, Employee employee) {
        List<EmployerPosting> result = new ArrayList<>();

        postings.forEach(posting -> {
            posting.setEmployer(null);

            boolean hadPosted = posting.getEmployeePostings().stream()
                    .filter(p -> p.getEmployee().getEmployeeId().equals(employee.getEmployeeId())).count() > 0;

            if (!hadPosted) {
                result.add(posting);
            }

            posting.setEmployeePostings(null);
        });

        return result;
    }


}
