package se.sprinta.headhunterbackend.ad.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.ad.Ad;
import se.sprinta.headhunterbackend.ad.dto.AdDtoForm;

/**
 * Ad data needs to be mapped to Ad entity format.
 */

@Component
public class AdDtoFormToAdConverter implements Converter<AdDtoForm, Ad> {

    /**
     * Converts a given AdDtoForm to an Ad entity
     *
     * @param source AdDtoForm to convert
     * @return Ad entity with data from source
     */

    @Override
    public Ad convert(AdDtoForm source) {
        return new Ad(
                source.htmlCode()
        );
    }
}
