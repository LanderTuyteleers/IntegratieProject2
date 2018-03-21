package be.kdg.kandoe.common;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * Wrapper class used to retrieve the current date/time
 */

@Component
public class TimeProvider implements Serializable {
    public Date now() {
        return new Date();
    }
}