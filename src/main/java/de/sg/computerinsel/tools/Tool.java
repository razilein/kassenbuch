package de.sg.computerinsel.tools;

/**
 * @author Sita Geßner
 */
public enum Tool {

    KASSENBUCH("kassenbuch"), REPARATUR("reparatur");

    private final String name;

    Tool(final String name) {
        this.name = name;
    }

    public static Tool getByName(final String name) {
        Tool result = KASSENBUCH;
        for (final Tool tool : Tool.values()) {
            if (tool.getName().equals(name)) {
                result = tool;
                break;
            }
        }
        return result;
    }

    public String getName() {
        return name;
    }

}
