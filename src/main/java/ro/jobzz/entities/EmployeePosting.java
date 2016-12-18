package ro.jobzz.entities;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
public class EmployeePosting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer employeePostingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_posting_id", nullable = false)
    private EmployerPosting employerPosting;

    @Column(nullable = false)
    private Float price;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false)
    private Date date;

    private String comment;

    @Column(nullable = false)
    private Integer status;

    public EmployeePosting() {
    }

    public Integer getEmployeePostingId() {
        return employeePostingId;
    }

    public void setEmployeePostingId(Integer employeePostingId) {
        this.employeePostingId = employeePostingId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public EmployerPosting getEmployerPosting() {
        return employerPosting;
    }

    public void setEmployerPosting(EmployerPosting employerPosting) {
        this.employerPosting = employerPosting;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
