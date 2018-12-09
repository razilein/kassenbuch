package de.sg.computerinsel.tools.rest;

import java.util.HashMap;
import java.util.Map;

import de.sg.computerinsel.tools.rest.model.TableData;
import lombok.Data;

@Data
public class SearchData {

    private TableData data;
    
    private Map<String, String> conditions = new HashMap<>();
}
