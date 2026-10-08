package multi_tenant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private PlanTYpe name;

    @Column(nullable = false)
    private int maxUsers;

    @Column(nullable = false)
    private int maxProjects;

    @Column(nullable = false)
    private int maxCustomers;

    @Column(nullable = false)
    private double price;

    public Plan() {
    }

    public Plan(
            PlanTYpe name,
            int maxUsers,
            int maxProjects,
            int maxCustomers,
            double price) {

        this.name = name;
        this.maxUsers = maxUsers;
        this.maxProjects = maxProjects;
        this.maxCustomers = maxCustomers;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public PlanTYpe getName() {
        return name;
    }

    public void setName(PlanTYpe name) {
        this.name = name;
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    public int getMaxProjects() {
        return maxProjects;
    }

    public void setMaxProjects(int maxProjects) {
        this.maxProjects = maxProjects;
    }

    public int getMaxCustomers() {
        return maxCustomers;
    }

    public void setMaxCustomers(int maxCustomers) {
        this.maxCustomers = maxCustomers;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
