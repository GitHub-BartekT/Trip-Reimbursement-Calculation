package pl.iseebugs.TripReimbursementApp.model.projection;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import pl.iseebugs.TripReimbursementApp.model.Reimbursement;

import java.time.LocalDate;

public class ReimbursementWriteModel {
    private int id;
    @NotNull(message = "Reimbursement name couldn't be empty.")
    @Size(max = 100, message = "Reimbursement name is too long.")
    private String name;
    @PastOrPresent(message = "Start date should be in the past or present.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @PastOrPresent(message = "End date should be in the past or present.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull (message = "No end date.")
    private LocalDate endDate;
    @Positive(message = "Distance should be positive.")
    private int distance;
    private boolean pushedToAccept;
    @NotNull
    private int userId;

    public ReimbursementWriteModel(){};

    public ReimbursementWriteModel(Reimbursement reimbursement) {
        id = reimbursement.getId();
        name = reimbursement.getName();
        startDate = reimbursement.getStartDate();
        endDate = reimbursement.getEndDate();
        distance = reimbursement.getDistance();
        pushedToAccept = reimbursement.isPushedToAccept();
        userId = reimbursement.getUser().getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public boolean isPushedToAccept() {
        return pushedToAccept;
    }

    public void setPushedToAccept(boolean pushedToAccept) {
        this.pushedToAccept = pushedToAccept;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
