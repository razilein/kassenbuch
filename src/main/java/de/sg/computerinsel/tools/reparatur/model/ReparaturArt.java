package de.sg.computerinsel.tools.reparatur.model;

/**
 * @author Sita Geßner
 */
public enum ReparaturArt {
    REPARATUR(0, "Reparatur"), GARANTIEPRUEFUNG(1, "Garantieprüfung"), GARANTIEFALL(2, "Garantiefall");

    private final int code;

    private final String description;

    ReparaturArt(final int code, final String description) {
        this.code = code;
        this.description = description;
    }

    public static ReparaturArt getByCode(final int code) {
        ReparaturArt art = REPARATUR;
        for (final ReparaturArt reparaturArt : ReparaturArt.values()) {
            if (code == reparaturArt.getCode()) {
                art = reparaturArt;
                break;
            }
        }
        return art;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }

}
