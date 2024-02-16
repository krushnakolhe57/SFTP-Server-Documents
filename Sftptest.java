package com.sftp;

import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class Sftptest {
	
	public static String host = "167.172.220.75"; 
	public static int port = 22; 
	public static String user = "root"; 
	public static String password = "Ariantech@317#"; 
	public static String dir = "/var/www/ariantechsolutions.com/html/omkarPanage/first-Spring-Boot-APP/FTPALLDownloads";
	public static String localDir = "D:/sftpDownload/";
	
	/*************************************************************************************************/
	
	public static void main(String[] args) {
		
		System.out.println(" Start>>>>>>>> ");
		
		JSch jSch = new JSch();

		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;

		try {
			session = jSch.getSession(user, host, port);
			session.setPassword(password);
		

			Properties conf = new Properties();
			conf.put("StrictHostKeyChecking", "no");

			session.setConfig(conf);
			session.connect();
			

			channel = session.openChannel("sftp");
			channel.connect();

			channelSftp = (ChannelSftp) channel;
		
			SftpServise.downloadPDF(dir , localDir , channelSftp , null );
		
			System.out.println( " Complated >>>>>>>>>");

		}
		catch (Exception e) {
			System.out.println("Exception Massage : " + e.getMessage());
			e.printStackTrace();
		}
		finally {

			if (channelSftp != null && channelSftp.isConnected()) {
				channelSftp.disconnect();
			}
			if (channel != null && channel.isConnected()) {
				channel.disconnect();
			}
			if (session != null && session.isConnected()) {
				session.disconnect();
			}
			channelSftp.exit();
		}

	}

}
