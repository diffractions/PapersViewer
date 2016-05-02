package controllers;

import java.io.File;
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

public class FileResponseController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String PAGE_OK = "/resources/DirectoryView.jsp";
	private static final String PAGE_ERROR = "404.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// println(
		// + ">>>  Response from file or directory");

		try {
			String absolutePath = req.getRequestURI().substring(
					req.getSession().getServletContext().getContextPath()
							.getBytes().length);
			// println(">>>  Absolute path : " + absolutePath);
			String path = getServletContext().getRealPath(absolutePath)
					.replace("%20", " ");
			// .println(">>>  Real Path : " + path);
			File file = new File(path);

			if (file.exists()) {

				if (file.isDirectory()) {

					// println(">>>  Directory found");
					// println(">>> Add files from folder in request attribute");
					req.setAttribute("filesChildren", file.listFiles());

					// println(">>>  Redirect to " + PAGE_OK);
					getServletContext().getRequestDispatcher(PAGE_OK).include(
							req, resp);

				} else {
					// println(">>>  Fille found");
					// println(">>>  Write file in output stream");
					try (FileInputStream fileInputStream = new FileInputStream(
							file);
							FileChannel fileChannel = fileInputStream
									.getChannel();
							OutputStream os = resp.getOutputStream();) {

						ByteBuffer buffer = ByteBuffer
								.allocate((int) fileChannel.size());
						fileChannel.read(buffer);
						resp.setContentLengthLong(fileChannel.size());
						os.write(buffer.array());
					}
				}
				return;
			}
		} catch (FileNotFoundException | NullPointerException e) {
			// NOP
		}

		// println(">>>  Fille not found");
		// println(">>>  Redirect to " + PAGE_ERROR);
		getServletContext().getRequestDispatcher("/" + PAGE_ERROR).include(req,
				resp);

	}
}
