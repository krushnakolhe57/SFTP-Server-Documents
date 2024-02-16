package com.sftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.stream.Stream;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jcraft.jsch.ChannelSftp;

public class SftpServise {
	
	
	
	
	public static void downloadPDF(String filepath, String localDir, ChannelSftp channelSftp, 
																Object filename)
																throws Exception {
		
		channelSftp.cd(filepath);
		
		  File allinOne = new File("D:\\sftpDownload_Merge"+ File.separator + filename );
			allinOne.mkdir();                                                                  // for create folder
			PDFMergerUtility ut = new PDFMergerUtility();
			String input = allinOne + File.separator + filename+"_mergeFile.pdf";
			String output = allinOne + File.separator +filename+"Table.pdf";   // for  create index page
			ut.setDestinationFileName( input );
			
			Document document = new Document();                // for index
			PdfWriter.getInstance(document, new FileOutputStream(output));

			document.open();

			PdfPTable table = new PdfPTable(2);
			addTableHeader(table );
			 Integer pageNo = 2;
			
			List<InputStream> source = new ArrayList<InputStream>();   // for store all  pdf
			
		 
		 Vector<ChannelSftp.LsEntry> ls = channelSftp.ls("/"+filepath);
	
		 Collections.sort(ls, new Sortbyname());
		
		 if (ls != null && !ls.isEmpty()) {
			 
			 for (ChannelSftp.LsEntry fl : ls   ) {
				 
				 if(fl!= null) {
					 String fname = fl.getFilename(); 
					 String fPath = filepath+"/"+fname;
					 String localDirectory = localDirectory(localDir, filename);
					 
					 	if (fname.equals(".") || fname.equals("..")) {
			                continue;
			            }
					 	
				
					 
					 	if( fl.getAttrs().isDir()) {
					 		downloadPDF(fPath , localDirectory , channelSftp , fname );
					 	}else {
					 		
					 		pageNo = downloadAndMergePDF(channelSftp ,  source, ut, fPath, localDirectory , table , pageNo);
					 	}
				 }
			 }
		 }
		 document.add(table);
			document.close();
			
			File index = new File(output);
			
			ut.addSource(index);
			ut.addSources(source);
			
		ut.mergeDocuments(null);
		index.delete();
		doProtect(input);
	}
	
	
	public static String localDirectory(String localDir , Object filename) {
		
		 String localDirectory = null;
		 
		 if (filename != null) {
             localDirectory = localDir + "/" + filename;
             File folder = new File(localDirectory);
             if (!folder.exists() && !folder.mkdirs()) {
                 System.out.println("Failed to create the local directory: " + localDirectory);
             }
         } else {
             localDirectory = localDir;
         }
		 return localDirectory;
	}
	
	public static  Integer downloadAndMergePDF(ChannelSftp channelSftp , 
																				List<InputStream> source ,
																				PDFMergerUtility ut ,
																				String fPath , 
																				String localDirectory,
																				PdfPTable table , Integer pageNo  ) throws Exception {
		
			channelSftp.get(fPath , localDirectory+"/");
	
		 String localFilePath = localDirectory + "/" + fPath.substring(fPath.lastIndexOf("/") + 1);
           File localFile = new File(localFilePath);

           if (localFile.exists()) {
        	   if( !localFilePath.toLowerCase().endsWith(".pdf")) {
       			localFile = CovertIntoPDF.convertToPDF(localFilePath );
       		 }
       		
       		source.add(new FileInputStream(localFile));
       		pageNo = 	addRows(table , localFile , pageNo);
           }
           return pageNo;
	}
	
	
	public static void doProtect(String input) throws IOException {
		
		File myfile = new File(input); 
		
		if(myfile.exists()) {
			
			PDDocument doc = PDDocument.load(myfile);   
			
			 AccessPermission access = new AccessPermission();
			 
			 access.setCanPrint(true);
			
			 StandardProtectionPolicy security = new StandardProtectionPolicy("krish", "1234", access);
			 security.setEncryptionKeyLength(128);
			 security.setPermissions(access);
			 doc.protect(security);
			 doc.save( myfile);
			 doc.close();
			
		}
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
	
	private static Integer addRows(PdfPTable table , File file , Integer pageNo ) throws IOException {
		
		 int i = Sortbyname.getNumericLength(file.getName());
	      
	    table.addCell(file.getName().substring(0, i));
	    table.addCell(String.valueOf(pageNo));
	    
	    PDDocument dd = PDDocument.load(file);
	    
	    pageNo = pageNo + dd.getNumberOfPages();
	  dd.close();
	  return pageNo;
	 
	}
}
