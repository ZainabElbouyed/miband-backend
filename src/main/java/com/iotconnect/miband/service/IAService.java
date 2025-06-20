package com.iotconnect.miband.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class IAService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String genererAvecOpenAI(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(System.getenv("OPENAI_API_KEY")); // Clé stockée en variable d’environnement

        String body = "{"
                + "\"model\": \"gpt-4\","
                + "\"messages\": ["
                + "  {\"role\": \"system\", \"content\": \"Tu es un assistant IA médical\"},"
                + "  {\"role\": \"user\", \"content\": \"" + prompt + "\"}"
                + "]"
                + "}";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.openai.com/v1/chat/completions", request, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            String content = root.path("choices").get(0).path("message").path("content").asText();

            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la génération IA.";
        }
    }

    public String construirePromptAlertes(Map<String, Object> donnees) {
        StringBuilder sb = new StringBuilder();
        sb.append("Tu es un assistant médical intelligent. Analyse ces données de santé :\n");
        donnees.forEach((k, v) -> sb.append(k).append(": ").append(v).append("\n"));
        sb.append("\nGénère une liste d’alertes au format JSON avec champs : date (ISO 8601), severity (info, warning, critical), message.\n");
        sb.append("Répond uniquement avec ce JSON.");
        return sb.toString();
    }

    public String construirePromptRapport(Map<String, Object> donnees) {
        StringBuilder sb = new StringBuilder();
        sb.append("Tu es un assistant médical expert. Génère un rapport médical complet en HTML structuré avec sections et recommandations.\n");
        sb.append("Voici les données patient :\n");
        donnees.forEach((k, v) -> sb.append("- ").append(k).append(": ").append(v).append("\n"));
        return sb.toString();
    }
}
