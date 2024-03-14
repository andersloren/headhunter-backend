package se.sprinta.headhunterbackend.system;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Customized detailed controller response.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    /**
     * flag is true if the request was successful, otherwise false
     */

    private boolean flag;

    /**
     * code is a customized Http code (see StatusCode)
     */

    private Integer code;

    /**
     * message is a customized message that uses human language to explain how the request turned out
     */

    private String message;

    /**
     * The actual data that was requested. Usually empty if an exception was thrown.
     */

    private Object data;

    public Result(boolean flag, Integer code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

}
