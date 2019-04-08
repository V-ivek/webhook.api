package com.amdtelecom.facebookmessenger.webhook.controller;

import com.amdtelecom.facebookmessenger.webhook.model.WebhookEvent;
import com.amdtelecom.facebookmessenger.webhook.service.WebhookService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller mapped to recieve webhook_event related payloads from facebook. For the below functionalities
 * <ul>
 * <li> To recieve and post appropriate to appropriate routee callback url to send Message status reports</li>
 * <li> To recieve and post appropriate to appropriate routee callback url to send Message inbound reports</li>
 * </ul>
 *
 * @author Vivek Selvarajan
 *
 */
@RestController
public class WebhookController {

    private static final Logger log = LogManager.getLogger();

    private static final String VERIFY_TOKEN = "amdfacebookmessengertoken";

    @Autowired
    WebhookService webhookService;

    @PostMapping(value = "/webhook")
    public ResponseEntity<String> webhookEndpoint(@RequestBody WebhookEvent webhookEvent) {
        log.info("POST with /webhook");
        if(webhookEvent.getObject().equals("page")) {
            webhookService.processWebhookEvents(webhookEvent);
            return new ResponseEntity<String>("EVENT_RECIEVED", HttpStatus.OK);
        } else {
        }
        return new ResponseEntity<>( HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/webhook")
    public ResponseEntity<String> webhookVerification(@RequestParam("hub.verify_token") String token,
                                                      @RequestParam("hub.challenge") String challenge,
                                                      @RequestParam("hub.mode") String mode) {
        log.info("GET with /webook");
        if(mode != null && token != null) {
            if (mode.equals("subscribe") && token.equals(VERIFY_TOKEN)){
                System.out.println("WEBHOOK_VERIFIED");
                return new ResponseEntity<String>(challenge, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @GetMapping(value = "/")
    public ResponseEntity<String> welcome(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
