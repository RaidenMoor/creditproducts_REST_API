package com.example.restbank.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "credit_applications")
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@SequenceGenerator(name = "default_gen", sequenceName = "applications_seq", allocationSize = 1, initialValue = 12)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "jsonId")
public class CreditApplication extends GenericModel{
    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", foreignKey = @ForeignKey(
            name = "fk_credit_application_client",
            foreignKeyDefinition = "FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE"
    ))
    private Client client;

    @ManyToOne(optional = false)
    @JoinColumn(name = "credit_product_id", foreignKey = @ForeignKey(
            name = "fk_credit_application_product",
            foreignKeyDefinition = "FOREIGN KEY (credit_product_id) REFERENCES credit_products(id) ON DELETE RESTRICT"
    ))
    private CreditProduct creditProduct;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "term_months", nullable = false)
    private int termMonths;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(name = "application_date", nullable = false)
    private LocalDate applicationDate;

    @Column(name = "decision_date")
    private LocalDate decisionDate;

    @OneToOne(mappedBy = "creditApplication", cascade = CascadeType.ALL)
    private IssuedLoan issuedLoan;
}
