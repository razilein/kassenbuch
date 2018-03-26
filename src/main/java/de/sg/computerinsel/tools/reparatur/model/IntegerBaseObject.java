package de.sg.computerinsel.tools.reparatur.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class IntegerBaseObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Object[] getTableModelObject() {
        return new Object[id];
    }

    public Object[] getTableModelObjectSearch() {
        return new Object[id];
    }

}
