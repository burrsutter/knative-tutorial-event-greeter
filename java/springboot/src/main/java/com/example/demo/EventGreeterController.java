package com.example.demo;

import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * EventGreeterController
 */
@RestController
public class EventGreeterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventGreeterController.class);

    private final SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss");

    private static final String RESPONSE_STRING_FORMAT = "BurrXX => '%s' : %d \n";

    private static final String HOSTNAME =
            parseContainerIdFromHostname(System.getenv().getOrDefault("HOSTNAME", "unknown"));

    static String parseContainerIdFromHostname(String hostname) {
        return hostname.replaceAll("event-greeter-v\\d+-", "");
    }


    /**
     * Counter to help us see the lifecycle
     */
    private int count = 0;

    @PostMapping("/")
    public @ResponseBody  String greet(@RequestBody String cloudEventJson) {
        count++;
        String greeterHost = String.format(RESPONSE_STRING_FORMAT, HOSTNAME, count);
        JsonObject response = new JsonObject(cloudEventJson)
                .put("host",greeterHost.replace("\n",""))
                .put("time",SDF.format(new Date()));
        LOGGER.info("Event Message Received \n {}",response.encodePrettily());
        return response.encode();
    }

    @GetMapping("/healthz")
    public String health() {
        return "OK";
    }
}
