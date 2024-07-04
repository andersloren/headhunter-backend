package se.sprinta.headhunterbackend.ad.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.ad.Ad;
import se.sprinta.headhunterbackend.ad.dto.AdDtoView;

/**
 * When requesting an ad, the ad data needs to be mapped to Ad dto view-object.
 * Some fields may be left out to prevent sensitive or irrelevant information from leaking out of the database.
 */

@Component
public class AdToAdDtoView implements Converter<Ad, AdDtoView> {

    /**
     * Converts a given AdDtoForm to an Ad entity.
     * Excludes Job field (ManyToOne-relation) to prevent recursion.
     *
     * @param source Ad to convert
     * @return AdDtoView object with data from source
     */

    @Override
    public AdDtoView convert(Ad source) {
        return new AdDtoView(
                source.getId(),
                source.getCreateDate(),
                source.getHtmlCode());
    }
}
