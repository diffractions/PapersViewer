package utility;

//import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
//import java.io.File;
//import java.io.FileInputStream;
import java.io.IOException;
//import java.io.OutputStream;
//import java.net.ServerSocket;
//import java.net.Socket;

//import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
//import org.apache.pdfbox.pdmodel.font.PDFont;
//import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.PDFTextStripper;

class SearchInPDF {

	public static void main(String[] args) {

		int count = 0;
		String filesPath = "I:\\working file\\literature\\papers";
		// PDDocument doc = PDDocument.load(filesPath + "\\" + "1 (1).pdf");

		// if (isSt(new PDFTextStripper().getText(doc),"si-o") || isSt(new
		// PDFTextStripper().getText(doc),"Si�o"))
		// System.out.println("[cont]");

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

				if (isSt(new PDFTextStripper().getText(doc), "c-o")
						|| isSt(new PDFTextStripper().getText(doc), "c–o"))
					System.out.println("+++++++++++++++++++++");
				doc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static boolean isSt(String compare, String find) {
		for (int i = 0; i < compare.length(); i++) {
			if (compare.regionMatches(true, i, find, 0, find.length())) {
				return true;
			}
		}
		return false;
	}

}


