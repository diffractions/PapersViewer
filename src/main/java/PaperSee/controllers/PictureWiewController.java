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

public class PictureWiewController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String relativeWebPath = getInitParameter("picturePath");
		String absoluteDiskPath = getServletContext().getRealPath(
				relativeWebPath);

		File file = new File(absoluteDiskPath);

		FileInputStream fileInputStream = new FileInputStream(file);
		FileChannel fileChannel = fileInputStream.getChannel();

		resp.setContentType("image/jpeg");
		resp.setContentLengthLong(fileChannel.size());

		ByteBuffer buffer = ByteBuffer.allocate((int) fileChannel.size());
		fileChannel.read(buffer);

		OutputStream os = resp.getOutputStream();
		os.write(buffer.array());
		os.close();
		fileChannel.close();
		fileInputStream.close();

	}
}
