package com.iotconnect.miband.controllers;
import com.iotconnect.miband.service.IAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/ia")
@CrossOrigin(origins = "*") // Remplace par ton URL Firebase
public class IAController {

    @Autowired
    private IAService iaService;

    @PostMapping("/alertes")
    public ResponseEntity<String> genererAlertes(@RequestBody Map<String, Object> donneesClient) {
        String prompt = iaService.construirePromptAlertes(donneesClient);
        String alertesJson = iaService.genererAvecOpenAI(prompt);
        return ResponseEntity.ok(alertesJson);
    }

    @PostMapping("/rapport")
    public ResponseEntity<String> genererRapport(@RequestBody Map<String, Object> donneesClient) {
        String prompt = iaService.construirePromptRapport(donneesClient);
        String rapportHtml = iaService.genererAvecOpenAI(prompt);
        return ResponseEntity.ok(rapportHtml);
    }
}
