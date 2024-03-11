package se.sprinta.headhunterbackend.ad.dto;

import java.time.ZonedDateTime;

public record AdDtoView(
        String id,
        ZonedDateTime createdDateTime,
        String htmlCode
) {

}
