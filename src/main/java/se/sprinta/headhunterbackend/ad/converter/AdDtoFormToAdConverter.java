package se.sprinta.headhunterbackend.ad.converter;

import org.springframework.core.convert.converter.Converter;
import se.sprinta.headhunterbackend.ad.Ad;
import se.sprinta.headhunterbackend.ad.dto.AdDtoForm;

public class AdDtoFormToAdConverter implements Converter<AdDtoForm, Ad> {

    @Override
    public Ad convert(AdDtoForm source) {
        return new Ad(source.htmlCode()
        );
    }
}
