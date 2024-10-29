package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {


    private static final String API_KEY = "e23e410a-d6dd-48a9-9d0f-3377d3e7b16a";

    public static void main(String[] args) {
        int limit = 7;

        try {
            String urlString = "https://api.weather.yandex.ru/v2/forecast?lat=55.75&lon=37.62";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("X-Yandex-API-Key", API_KEY);
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(response.toString());

                System.out.println("Полный ответ от сервера: ");
                System.out.println(jsonResponse.toString(2));

                JSONObject fact = jsonResponse.getJSONObject("fact");
                int currentTemp = fact.getInt("temp");
                System.out.println("Текущая температура: " + currentTemp + "°C");

                JSONArray forecasts = jsonResponse.getJSONArray("forecasts");
                int totalTemp = 0;

                for (int i = 0; i < forecasts.length(); i++) {
                    JSONObject dayForecast = forecasts.getJSONObject(i).getJSONObject("parts").getJSONObject("day");
                    totalTemp += dayForecast.getInt("temp_avg");
                }

                double averageTemp = (double) totalTemp / limit;
                System.out.println("Средняя температура за " + limit + " дней: " + averageTemp + "°C");
            } else {
                System.out.println("Ошибка: Не удалось выполнить запрос. Код ответа: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
