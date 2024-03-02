package se.sprinta.headhunterbackend.job.dto;

import se.sprinta.headhunterbackend.ad.Ad;

import java.util.List;

public record JobDtoView(
        Long id,
        String title,
        String description,
        String email,
        String htmlCode,
        String instruction
) {
}

