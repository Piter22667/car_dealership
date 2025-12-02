package org.example.car_dealership.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.car_dealership.dto.ChatGPTRequest;
import org.example.car_dealership.dto.ChatGPTResponse;
import org.example.car_dealership.dto.PromptDTO;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.*;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatGPTServiceImpl {

    private final WebClient webClient;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.model}")
    private String model;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPass;

    public ChatGPTServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    // --------------------  JSON EXPORT FROM POSTGRES ---------------------

    public static String tableToJson(Connection conn, String tableName, String where)
            throws SQLException, JsonProcessingException {

        if (!tableName.matches("[a-zA-Z0-9_\\.]+")) {
            throw new IllegalArgumentException("Invalid table name: " + tableName);
        }

        String sql = "SELECT * FROM " + tableName;
        if (where != null && !where.trim().isEmpty()) {
            sql += " WHERE " + where;
        }

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();

            List<Map<String, Object>> rows = new ArrayList<>();

            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columns; i++) {

                    String colName = md.getColumnLabel(i);
                    Object val = rs.getObject(i);

                    if (val instanceof Timestamp ts) {
                        row.put(colName, ts.toInstant().atOffset(ZoneOffset.UTC).toString());
                    } else if (val instanceof Date d) {
                        row.put(colName, d.toString());
                    } else if (val instanceof Array arr) {
                        try {
                            row.put(colName, arr.getArray());
                        } finally {
                            try { arr.free(); } catch (Exception ignored) {}
                        }
                    } else if (val instanceof PGobject pg) {
                        if ("json".equals(pg.getType()) || "jsonb".equals(pg.getType())) {
                            try {
                                ObjectMapper mapper = new ObjectMapper();
                                row.put(colName, mapper.readValue(pg.getValue(), Object.class));
                            } catch (Exception e) {
                                row.put(colName, pg.getValue());
                            }
                        } else {
                            row.put(colName, pg.getValue());
                        }
                    } else {
                        row.put(colName, val);
                    }
                }
                rows.add(row);
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            return mapper.writeValueAsString(rows);
        }
    }

    private String loadTableJson() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass)) {
            return tableToJson(conn, "cars", null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // --------------------  MAIN CHATGPT LOGIC ---------------------

    public String getChatResponse(PromptDTO userInput) {

        // 1. Отримуємо JSON з таблиці
        String tableJson = loadTableJson();

        // 2. Формуємо фінальний промпт

// 2. Формуємо фінальний промпт
        String finalPrompt =
                "Запит клієнта: " + userInput.prompt()
                        + "\n\n---\nДля відповіді використовуй наступний **список доступних автомобілів** (у форматі JSON). **Проаналізуй** його, знайди відповідні авто і **сформуй** професійну відповідь:\n"
                        + tableJson;

// 3. Обмеження на кількість слів + відправка таблиці

        ChatGPTRequest request = new ChatGPTRequest(
                model,
                List.of(
                        new ChatGPTRequest.Message(
                                "system",
                                "Ти — досвідчений та ввічливий консультант автосалону. Твоя головна задача — надати клієнту максимально корисну, релевантну інформацію про автомобілі, використовуючи надані JSON-дані. Відповідь має бути завжди заснована лише на цих даних. Дотримуйся наступних правил: 1. **Стиль:** Ввічливий, професійний, дружній тон. 2. **Формат:** Відповідай простим, неформатованим текстом. Не використовуй символи жирного шрифту, курсиву, маркованих списків або заголовків. Просто перерахуй інформацію з переносами рядка. 3. **Обмеження:** Відповідь не повинна перевищувати 140 слів."), // ... (решта коду залишається такою ж)
                        new ChatGPTRequest.Message(
                                "user",
                                finalPrompt
                        )
                )
        );

        ChatGPTResponse response = webClient.post()
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatGPTResponse.class)
                .block();

        return response.choices().get(0).message().content();
    }
}
