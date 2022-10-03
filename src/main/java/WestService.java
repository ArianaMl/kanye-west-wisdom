import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class WestService {

    private final String westKanyeURL = "https://api.kanye.rest";
    private final String query = "INSERT INTO wisdom (kanye_wisdom) VALUES (?)";
    private final String checkIfExistsQuery = "SELECT kanye_wisdom FROM wisdom WHERE kanye_wisdom = ?";

    private int repeatCounter;
    JDBCPostgreSQLConnection jdbcConnection = new JDBCPostgreSQLConnection();

    public void displayAndUpdate() throws SQLException, JSONException {

        Connection connection = jdbcConnection.connect();
        String kanyeWisdom = getWisdom();

        if (connection != null) {

            if (!isRepeated(kanyeWisdom, connection)) {

                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, kanyeWisdom);
                statement.executeUpdate();
                System.out.println(kanyeWisdom);
                statement.close();
            } else if (repeatCounter < 4) {
                repeatCounter++;
                displayAndUpdate();
            } else {
                System.out.println("No more new wisdom");
            }
            jdbcConnection.connect().close();
        } else {
            System.out.println("Please check your connection with database.");
        }
    }

    private String getWisdom() throws JSONException {
        JSONObject jsonObject = new JSONObject(getHttpResponse());
        return jsonObject.getString("quote");
    }

    private boolean isRepeated(String sentense, Connection connection) throws SQLException {

        PreparedStatement statementExistQuery = connection.prepareStatement(checkIfExistsQuery);
        statementExistQuery.setString(1, sentense);
        ResultSet rs = statementExistQuery.executeQuery();

        while (rs.next()) {
            return rs.getString("kanye_wisdom").equals(sentense);
        }
        return false;
    }

    private String getHttpResponse() {

        StringBuilder responseJson = new StringBuilder();

        try {
            URL url = new URL(westKanyeURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == 200) {
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    responseJson.append(scanner.next()+" ");
                }
                scanner.close();

            } else {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return responseJson.toString();
    }
}
