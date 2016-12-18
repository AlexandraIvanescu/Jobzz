package ro.jobzz.constants;


public enum EmployeePostStatus {
    WAITING_NO_RESPONSE(0), WAITING_ACCEPTED(1), WAITING_REFUSED(2), IN_PROGRESS(3),
    IN_PROGRESS_REMOVED(4), DONE_WAITING(5), DONE_PAID(6), CLOSED(7);

    private Integer status;

    EmployeePostStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
