import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataParse {
	private static final String url = "jdbc:mysql://localhost:3306/pettrip?serverTimezone=UTC";
	private static final String user = "ssafy";
	private static final String pass = "ssafy";

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, pass);
	}

	public static void close(AutoCloseable... closeables) {
		for (AutoCloseable c : closeables) {
			if (c != null) {
				try {
					c.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws IOException, ParseException, SQLException {

		String rootPath = System.getProperty("user.dir");
		String filePath = rootPath + "/src/parser/data.json";

		BufferedReader bf = new BufferedReader(new FileReader(filePath));

		String data;

		StringBuilder sb = new StringBuilder();
		while ((data = bf.readLine()) != null) {
			sb.append(data);
		}

		data = sb.toString();
				
		JSONParser jsonParser = new JSONParser();
		JSONArray jsonArray = (JSONArray) jsonParser.parse(data);
		
		int cnt = 0;
		
		for (int i = 0; i < jsonArray.size(); i++) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			
			System.out.println(jsonArray.get(i));
			
			try {
				JSONObject obj = (JSONObject) jsonArray.get(i);
				String sql = "INSERT INTO pet_attractions(petTursmInfo, relaAcdntRistMtr, acmpyTypeCd, relaPosesFclty, relaFrnshPrdlst, etcAcmpyInfo, relaPurcPrdlst, acmpyPsblCpam, relaRntlPrdlst, acmpyNeedMtr, content_id)"
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);\r\n";

				conn = getConnection();
				pstmt = conn.prepareStatement(sql);

				if (obj.get("petTursmInfo").toString().isBlank() && obj.get("relaAcdntRiskMtr").toString().isBlank()
						&& obj.get("acmpyTypeCd").toString().isBlank() && obj.get("relaPosesFclty").toString().isBlank()
						&& obj.get("relaFrnshPrdlst").toString().isBlank() && obj.get("etcAcmpyInfo").toString().isBlank()
						&& obj.get("relaPurcPrdlst").toString().isBlank() && obj.get("dog_type").toString().isBlank()
						&& obj.get("relaRntlPrdlst").toString().isBlank() && obj.get("acmpyNeedMtr").toString().isBlank()) {
					cnt++;
					continue;
				}

				pstmt.setString(1,
						obj.get("petTursmInfo").toString().isBlank() ? null : obj.get("petTursmInfo").toString());
				pstmt.setString(2,
						obj.get("relaAcdntRiskMtr").toString().isBlank() ? null : obj.get("relaAcdntRiskMtr").toString());
				pstmt.setString(3, obj.get("acmpyTypeCd").toString().isBlank() ? null : obj.get("acmpyTypeCd").toString());
				pstmt.setString(4,
						obj.get("relaPosesFclty").toString().isBlank() ? null : obj.get("relaPosesFclty").toString());
				pstmt.setString(5,
						obj.get("relaFrnshPrdlst").toString().isBlank() ? null : obj.get("relaFrnshPrdlst").toString());
				pstmt.setString(6,
						obj.get("etcAcmpyInfo").toString().isBlank() ? null : obj.get("etcAcmpyInfo").toString());
				pstmt.setString(7,
						obj.get("relaPurcPrdlst").toString().isBlank() ? null : obj.get("relaPurcPrdlst").toString());
				pstmt.setString(8, obj.get("dog_type").toString().isBlank() ? null : obj.get("dog_type").toString());
				pstmt.setString(9,
						obj.get("relaRntlPrdlst").toString().isBlank() ? null : obj.get("relaRntlPrdlst").toString());
				pstmt.setString(10,
						obj.get("acmpyNeedMtr").toString().isBlank() ? null : obj.get("acmpyNeedMtr").toString());
				pstmt.setString(11, obj.get("contentid").toString().isBlank() ? null : obj.get("contentid").toString());

				pstmt.execute();

			} finally {
				// TODO: handle finally clause
				close(pstmt, conn);
			}
		}
		System.out.println(cnt);
	}

}
