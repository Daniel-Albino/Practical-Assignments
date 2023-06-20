package pt.isec.tp_pd.data.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, Heello!";
    }

    @GetMapping("/hello2")
    public String hello2() {
        return "<p style=\"background: #f00;\">Hello, Heello!</P>";
    }

    @GetMapping("/hello/{LANG}")
    public ResponseEntity<String> helloLANG(@PathVariable("LANG") String LANG,
                                            @RequestParam(name = "name", required = false, defaultValue = "defaultName") String name) {
        String responseBody;

        switch (LANG.toUpperCase()) {
            case "UK" -> responseBody = "Hello " + name + "!";
            case "PT" -> responseBody = "<p style=\"font-size: 42px; background: #cba0a0;\">Olá " + name + "!</P>";
            case "FR" -> responseBody = "Salut " + name + "!";
            case "ES" -> responseBody = "Ola " + name + "!";
            default -> {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not supported language: " + LANG + ".");
            }
        }

        // return ResponseEntity.ok(responseBody);
        // OR
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }
}