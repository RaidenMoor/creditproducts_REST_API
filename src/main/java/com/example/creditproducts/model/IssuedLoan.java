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

@Entity
@Table(name = "issued_loans")
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@SequenceGenerator(name = "default_gen", sequenceName = "loans_seq", allocationSize = 1, initialValue = 12)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "jsonId")
public class IssuedLoan extends GenericModel{
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "monthly_payment", nullable = false)
    private BigDecimal monthlyPayment;

    @Column(name = "remaining_amount", nullable = false)
    private BigDecimal remainingAmount;

    @OneToOne(optional = false)
    @JoinColumn(name = "application_id", foreignKey = @ForeignKey(
            name = "fk_issued_loan_application",
            foreignKeyDefinition = "FOREIGN KEY (application_id) REFERENCES credit_applications(id) ON DELETE RESTRICT"
    ))
    private CreditApplication creditApplication;

}
