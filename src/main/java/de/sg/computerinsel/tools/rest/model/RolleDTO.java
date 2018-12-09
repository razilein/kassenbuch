package de.sg.computerinsel.tools.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RolleDTO {

    private Integer id;

    private String beschreibung;

    private boolean right;

}
