package se.sprinta.headhunterbackend.job;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sprinta.headhunterbackend.utils.HtmlUtilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class HtmlUtilitiesTest {

    private HtmlUtilities htmlUtilities;

    private String response;

    @BeforeEach
    void setUp() {
        this.htmlUtilities = new HtmlUtilities();
        this.response = "<!DOCTYPE html><html><body>generate content</body></html>";
    }

    @Test
    @DisplayName("makeHtmlResponseSubstring - Success")
    void testMakeHtmlResponseSubstringSuccess() {
        String htmlCode = this.htmlUtilities.makeHtmlResponseSubstring(this.response);
        assertEquals(htmlCode, this.response);
    }

    @Test
    @DisplayName("makeHtmlResponseSubstring - Null Argument (Exception)")
    void testMakeHtmlResponseSubstringNullResponse() {
        Throwable thrown = assertThrows(IllegalArgumentException.class,
                () -> this.htmlUtilities.makeHtmlResponseSubstring(null));

        Assertions.assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("AI response content cannot be null");
    }

    @Test
    @DisplayName("makeHtmlResponseSubstring - Not Valid HTML (Exception)")
    void testMakeHtmlResponseSubstringInvalidHTMLCode() {
        Throwable thrown = assertThrows(IllegalArgumentException.class,
                () -> this.htmlUtilities.makeHtmlResponseSubstring("Some Text"));

        Assertions.assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("AI response is not valid HTML code");
    }
}