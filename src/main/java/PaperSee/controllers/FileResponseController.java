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

//		System.out.println("______________________________________\n"
//				+ ">>>  Response from file or directory");

		try {
			String absolutePath = req.getRequestURI().substring(
					req.getSession().getServletContext().getContextPath()
							.getBytes().length);
//			System.out.println(">>>  Absolute path : " + absolutePath);
			String path = getServletContext().getRealPath(absolutePath)
					.replace("%20", " ");
//			System.out.println(">>>  Real Path : " + path);
			File file = new File(path);

			if (file.exists()) {

				if (file.isDirectory()) {

//					System.out.println(">>>  Directory found");
//					System.out
//							.println(">>> Add files from folder in request attribute");
					req.setAttribute("filesChildren", file.listFiles());

//					System.out.println(">>>  Redirect to " + PAGE_OK);
					getServletContext().getRequestDispatcher(PAGE_OK).include(
							req, resp);

				} else {
//					System.out.println(">>>  Fille found");
//					System.out.println(">>>  Write file in output stream");
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

//		System.out.println(">>>  Fille not found");
//		System.out.println(">>>  Redirect to " + PAGE_ERROR);
		getServletContext().getRequestDispatcher("/" + PAGE_ERROR).include(req,
				resp);

	}
}
