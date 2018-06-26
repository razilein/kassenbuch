package de.sg.computerinsel.tools.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Message {

    ERROR("error"), SUCCESS("success"), INFO("info"), WARNING("warning");

    private final String code;
}
