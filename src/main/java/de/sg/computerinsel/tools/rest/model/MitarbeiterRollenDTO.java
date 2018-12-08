package de.sg.computerinsel.tools.rest.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MitarbeiterRollenDTO {

    private Integer mitarbeiterId;

    private List<RolleDTO> rollen = new ArrayList<>();

}
