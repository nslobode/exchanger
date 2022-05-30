package org.exchange.exchanger.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "conversion")
public class Conversion {

    @Id
    private String id;
    private Double rate;
    private Double margin;
}
