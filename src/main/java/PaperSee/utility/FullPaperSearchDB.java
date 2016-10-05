package utility;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
//import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.Enumeration;
import java.sql.Connection;
import java.sql.Statement;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class FullPaperSearchDB {
	public static void main(String[] args) throws SQLException {

//		Enumeration<Driver> t = DriverManager.getDrivers();
//
//		while (t.hasMoreElements()) {
//			System.out.println(t.nextElement());
//		}

		Connection conn = DriverManager
				.getConnection("jdbc:mysql://127.0.0.1:3306/papers_search",
						"root", "si17st18");

		Statement stat = conn.createStatement();
		ResultSet rs = stat
				.executeQuery("SELECT paper_id FROM paper where text like '%c–o%' or text like '%c-o%';");
		while (rs.next()) {
			System.out.println(rs.getString(1));
		}

		rs.close();
		stat.close();

		// addPapers(conn);

	}

	@SuppressWarnings("unused")
	private static void addPapers(Connection conn) throws SQLException {

		int count = 0;
		String filesPath = "I:\\working file\\literature\\papers";

		PreparedStatement st = conn
				.prepareStatement("INSERT INTO paper VALUES (?, ?)");

		for (String f : new File(filesPath).list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith(".pdf") || name.endsWith(".PDF"))
					return true;
				return false;

			}
		})) {
			PDDocument doc;
			try {
				doc = PDDocument.load(filesPath + "\\" + f);

				System.out.println("[" + ++count + "] " + f + " "
						+ doc.getNumberOfPages());

				String text = new PDFTextStripper().getText(doc);
				text = text.replace("\'", "\\\'");
				System.out.println(text.length());

				try {
					st.setString(1, f);
					st.setString(2, text);
					System.out.println(st.executeUpdate());

				} catch (java.sql.SQLException e) {
					e.printStackTrace();
				}

				doc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		st.close();
		conn.close();
	}
}
