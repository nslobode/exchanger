package org.exchange.exchanger.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts_GBP")
@Data @Setter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor(force = true)
public class AccountGbp implements Account {

    @Id
    @Column(unique = true, nullable = false)
    @ToString.Include
    private String id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @JsonBackReference
    private User user;

    @ToString.Include
    private BigDecimal balance;
}
