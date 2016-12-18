package ro.jobzz.constants;


public enum EmployerPostStatus {
    WAITING(0), IN_PROGRESS(1), DONE(2), CLOSED(3);

    private Integer status;

    EmployerPostStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
