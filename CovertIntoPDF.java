package com.sftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.element.Image;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;

public class CovertIntoPDF {
	

	public static File convertToPDF(String docFile) throws Exception  {
		
		
		 String pdf = docFile.substring(0, docFile.lastIndexOf(".")) + ".pdf";
         File pdfFile = new File(pdf);

		    
		    if (pdfFile.exists()) {
		        System.out.println("PDF file already exists for: " + docFile);
		        return pdfFile;
		    }
		
	
	
	    	
	    	 if (docFile.toLowerCase().endsWith(".docx")) {
	    		 convertDocxToPdf(docFile , pdfFile );
	    	 }
	    	 else if (docFile.toLowerCase().endsWith(".html")) {
	    		 convertHtmlToPdf(docFile  , pdfFile);
	    	 }
	    	 else if (docFile.toLowerCase().endsWith(".jpg")) {
		            convertImageToPdf(docFile , pdfFile);
		        }
	    	 else if (docFile.toLowerCase().endsWith(".tiff")) {
	    		 convertImageToPdf(docFile , pdfFile);
		        }
	        else {
	        	return pdfFile;
	        }
	    	 
	    	 return pdfFile;
	    	
		
	}
	    	
	/*************************************************************************************************/
	
	
	private static void convertDocxToPdf(String docFile  , File pdfFile) throws Exception {
		
	
		 	    InputStream input = new FileInputStream(new File(docFile));
		 	   XWPFDocument document = new XWPFDocument(input);
		 	 
		 	  PdfOptions option = PdfOptions.create();
		 	    FileOutputStream out = new FileOutputStream(pdfFile);
		 	    
        PdfConverter.getInstance().convert(document, out, option);
        document.close();
        out.close();
          
    }
	
	
	/*************************************************************************************************/

	
	private static void convertHtmlToPdf(String docFile  , File pdfFile ) throws Exception {
		
		Document doc = new Document();
 	    InputStream input = new FileInputStream(docFile);
 	    FileOutputStream out = new FileOutputStream(pdfFile);

    	doc.open();
    	
		PdfWriter writer = PdfWriter.getInstance(doc, out);
    	writer.open();
    	 XMLWorkerHelper.getInstance().parseXHtml(writer, doc, input);
    	 writer.close();
    	 doc.close();
    	 out.close();
	}
	    
	   
	/*************************************************************************************************/
	
	
//	public static void convertImageToPdf(String docFile   , File pdfFile) throws Exception {
//		
//		    FileOutputStream out = new FileOutputStream(pdfFile);
//        
//            Document document = new Document();
//            PdfWriter.getInstance(document, out);
//            document.open();
//
//            Image image = Image.getInstance(docFile);
//            image.setDpi(72, 72);
//            document.add(image);
//
//            document.close();
//        
//	}
	

	    public static void convertImageToPdf(String imagePath, File pdfFilePath) throws Exception {
	    	
	      FileOutputStream out = new FileOutputStream(pdfFilePath) ;
	        	
	        	 com.itextpdf.kernel.pdf.PdfWriter pdfWriter = 
	        			 new  com.itextpdf.kernel.pdf.PdfWriter(out);
	        	 
	        	 Image image = new Image(ImageDataFactory.create(imagePath));
	         
	            		new  com.itextpdf.layout.Document( new PdfDocument(pdfWriter))
	            													.add(image)
	            													.close();

	        
	    }
	



}
