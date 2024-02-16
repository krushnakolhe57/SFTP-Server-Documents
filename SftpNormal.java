package com.sftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;





public class SftpNormal {
	
	static Integer pp = 2;

	public static void main(String[] args) throws Exception {

		System.out.println(" >..........Start...........<<<<");
		
	
		
		File one = new File("D:\\DummyPDF\\1_paper.pdf");
		File two = new File("D:\\DummyPDF\\2_paper.pdf");
		File thr = new File("D:\\DummyPDF\\3_paper.pdf");

		File all = new File("D:\\DummyPDF\\SFTPNormal");
		
		all.mkdir();
		
		PDFMergerUtility ut = new PDFMergerUtility();
		
		String newfile = all + File.separator + "newFile.pdf";
		
		ut.setDestinationFileName(newfile);
		
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(all + File.separator +"iTextTable.pdf"));

		document.open();

		PdfPTable table = new PdfPTable(2);
		addTableHeader(table );
		
	
		
		List<InputStream> source = new ArrayList<InputStream>();
		
		
		source.add(new FileInputStream(one));
		addRows(table  , one);

		
		source.add(new FileInputStream(two));
		addRows(table , two);
		
		source.add(new FileInputStream(thr));
		addRows(table , thr);
		
		document.add(table);
		document.close();
		
		File ff = new File(all + File.separator +"iTextTable.pdf");
		
		ut.addSource(ff);
		ut.addSources(source);
		
		ut.mergeDocuments(null);
		
		ff.delete();
	
	    
		File  myfile =  new File(all + File.separator + "newFile.pdf");
		
		PDDocument doc = PDDocument.load(myfile);   

		// for File password
		   
		 AccessPermission access = new AccessPermission();
		 access.setCanPrint(true);
		 StandardProtectionPolicy security = new StandardProtectionPolicy("krish", "1234", access);
		 security.setEncryptionKeyLength(128);
		 security.setPermissions(access);
		 doc.protect(security);
		 doc.save( myfile);
		
		System.out.println(" >..........Complate...........<<<<");
		
		 
	}
	
	
	private static void addTableHeader(PdfPTable table ) {
	    Stream.of("PDF No.", "Page No.")
	      .forEach(columnTitle -> {
	        PdfPCell header = new PdfPCell();
	        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        header.setBorderWidth(2);
	        header.setPhrase(new Phrase(columnTitle));
	        table.addCell(header);
	    });
	}
	
	private static void addRows(PdfPTable table , File file) throws IOException {
	    table.addCell(file.getName());
	    table.addCell(String.valueOf(pp));
	    
	    PDDocument dd = PDDocument.load(file);
	    
	  pp = pp + dd.getNumberOfPages();
	  
	  dd.close();
	 
	}
	


	
}
