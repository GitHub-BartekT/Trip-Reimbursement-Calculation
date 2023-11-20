package pl.iseebugs.TripReimbursementApp.model.projection;

import pl.iseebugs.TripReimbursementApp.model.Reimbursement;
import pl.iseebugs.TripReimbursementApp.model.User;
import pl.iseebugs.TripReimbursementApp.model.UserRepository;

import java.time.LocalDate;

public class ReimbursementDTO {
    private int id;
    private String name;
    private LocalDate start_date;
    private LocalDate end_date;
    private int distance;
    private boolean pushedToAccept;
    private int userId;

    public ReimbursementDTO(Reimbursement reimbursement) {
        id = reimbursement.getId();
        name = reimbursement.getName();
        start_date = reimbursement.getStart_date();
        end_date = reimbursement.getEnd_date();
        distance = reimbursement.getDistance();
        pushedToAccept = reimbursement.isPushedToAccept();
        userId = reimbursement.getUser().getId();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
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

    public Reimbursement toReimbursement (){
        var result = new Reimbursement();
        result.setId(id);
        result.setName(name);
        result.setStart_date(start_date);
        result.setEnd_date(end_date);
        result.setDistance(distance);
        result.setPushedToAccept(pushedToAccept);

        UserRepository repository = null;
        User user = new User();
        user = repository.findById(userId).orElse(null);
        assert user != null;
        result.setUser(user);
        return result;
    }


}
