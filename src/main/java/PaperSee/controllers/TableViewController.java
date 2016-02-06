package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TableViewController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {

			String relativeWebPath = getInitParameter("tableView");
			String absolutePath = getServletContext().getRealPath(
					relativeWebPath);

			File file = new File(absolutePath);
			FileInputStream fileInputStream = new FileInputStream(file);
			FileChannel fileChannel = fileInputStream.getChannel();

			resp.setContentType("text/html");
			resp.setContentLength((int) fileChannel.size());

			ByteBuffer buffer = ByteBuffer.allocate((int) fileChannel.size());

			fileChannel.read(buffer);
			buffer.rewind();

			byte[] b = buffer.array();
			OutputStream outputStream = resp.getOutputStream();
			outputStream.write(b);

			fileInputStream.close();
			fileChannel.close();
			outputStream.close();

		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
		}
	}
}
