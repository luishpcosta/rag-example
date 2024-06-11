package br.com.lhpc.rag.example.infrastructure.port.inbound.dto;

import java.util.UUID;

public record BookDto(String name, String author, String synopsis, String reasonToRead) {

}
