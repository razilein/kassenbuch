package de.sg.computerinsel.tools.reparatur.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class IntegerBaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @JsonIgnore
    public Object[] getTableModelObject() {
        return new Object[id];
    }

    @JsonIgnore
    public Object[] getTableModelObjectSearch() {
        return new Object[id];
    }

}
