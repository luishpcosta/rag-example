package br.com.lhpc.rag.example.infrastructure.port.inbound.dto;

public enum Seniority {

    INTERN("Intern"),
    JUNIOR("Junior"),
    MID_LEVEL("Mid Level"),
    SENIOR("Senior"),
    LEAD("Lead"),
    PRINCIPAL("Principal"),
    ARCHITECT("Architect");

    private final String description;

    Seniority(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
