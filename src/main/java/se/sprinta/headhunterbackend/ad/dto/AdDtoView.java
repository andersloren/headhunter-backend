package se.sprinta.headhunterbackend.ad.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Output Ad data format.
 *
 * @param id              The unique identifier of the Ad object.
 * @param dateCreated The timestamp for when the database entry was originally created.
 * @param htmlCode        The Ad content in HTML format.
 */

public record AdDtoView(
        String id,
        LocalDate dateCreated,
        String htmlCode
) {

}
