package controllers;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ElementsWiewController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger("LOG");
	
	public static final String ATTRIBUTE_FILE_PATH = "filePath";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {

			String relativeWebPath = getInitParameter("source");
			String mimeType = getInitParameter("mime-type");

			if (relativeWebPath != null && mimeType != null) {

				log.debug("FOUND FILE WITH TYPES");

				log.debug(">>>  Relative Web Path : " + relativeWebPath);
				String absolutePath = getServletContext().getRealPath(
						relativeWebPath);
				log.debug(">>>  Real Path : " + absolutePath);
				log.debug(
						">>> Set Content Type (mime-type) : " + getInitParameter("mime-type"));

				resp.setContentType(mimeType);
				writeFile(absolutePath, resp);

			} else {

				log.debug("TYPES OR FILE NOT FOUND, response by url");
				writeFile((String) req.getAttribute(ATTRIBUTE_FILE_PATH), resp);
			}
		} catch (IOException e) {
			log.fatal("", e);
		}
	}

	private void writeFile(String path, HttpServletResponse resp)
			throws IOException, FileNotFoundException {
		try (FileInputStream fileInputStream = new FileInputStream(path);
				FileChannel fileChannel = fileInputStream.getChannel();
				OutputStream os = resp.getOutputStream();) {

			log.debug(">>>  Write file in output stream");

			ByteBuffer buffer = ByteBuffer.allocate((int) fileChannel.size());
			fileChannel.read(buffer);
			resp.setContentLength((int)fileChannel.size());
			os.write(buffer.array());
		}
	}
}
