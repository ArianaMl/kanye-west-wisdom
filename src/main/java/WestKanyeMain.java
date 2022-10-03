import org.json.JSONException;

import java.sql.SQLException;

public class WestKanyeMain {

    public static void main(String[] args) throws JSONException, SQLException {

        WestService westService = new WestService();
        westService.displayAndUpdate();
    }
}
