package se.sprinta.headhunterbackend.utils;

import org.springframework.stereotype.Component;

@Component
public class HtmlUtilities {

    public String makeHtmlResponseSubstring(String response) {
        if (response == null) throw new IllegalArgumentException("AI response content cannot be null");

        int cutBeginning = response.indexOf("<!D");
        // Adjusting cutEnd to include the entire "</html>" tag
        int cutEnd = response.lastIndexOf("</html>") + "</html>".length();

        // Extracting the substring
        if (cutBeginning == -1 || cutEnd == -1 || cutEnd <= cutBeginning)
            throw new IllegalArgumentException("AI response is not valid HTML code");

        return response.substring(cutBeginning, cutEnd);
    }

}
