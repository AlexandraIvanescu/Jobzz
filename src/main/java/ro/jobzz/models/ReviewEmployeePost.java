package ro.jobzz.models;


import ro.jobzz.entities.EmployeePosting;
import ro.jobzz.entities.ReviewEmployee;

public class ReviewEmployeePost {

    private ReviewEmployee review;

    private EmployeePosting employeePost;

    public ReviewEmployee getReview() {
        return review;
    }

    public void setReview(ReviewEmployee review) {
        this.review = review;
    }

    public EmployeePosting getEmployeePost() {
        return employeePost;
    }

    public void setEmployeePost(EmployeePosting employeePost) {
        this.employeePost = employeePost;
    }
}
