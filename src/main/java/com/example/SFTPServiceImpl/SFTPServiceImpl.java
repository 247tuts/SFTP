package com.example.SFTPServiceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.SFTPService.SftpService;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

@Service
@Transactional
public class SFTPServiceImpl implements SftpService {

	@Value("${upload.directory}")
	private String uploadDirectory;

	@Value("${shftp.host}")
	private String host;

	private final int port = 22;

	@Value("${shftp.username}")
	private String user;

	@Value("${shftp.password}")
	private String password;
	
	//get the list of files name from the sftp server
	//this function will return the list of file's name
	public List<String> getList(String path) {
			List<String> fileNames=new ArrayList<String>();
			try {
				JSch jsch = new JSch();
				Session session = jsch.getSession(user, host, port);
				session.setPassword(password);
				session.setConfig("StrictHostKeyChecking", "no");
				System.out.println("Establishing Connection...");
				session.connect();
				System.out.println("Connection established.");
				System.out.println("Creating SFTP Channel.");
				ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
				sftpChannel.connect();
				System.out.println("SFTP Channel created.");

				sftpChannel.cd(path);
				Vector filelist = sftpChannel.ls(path);
				for (int i = 0; i < filelist.size(); i++) {
					LsEntry entry = (LsEntry) filelist.get(i);
	//just uncomment this line then your file name also start printing on the console	
	//System.out.println(entry.getFilename());
					fileNames.add(entry.getFilename());
				}
				session.disconnect();
				sftpChannel.disconnect();
			}

			catch (Exception e) {
				e.printStackTrace();
			}
			return fileNames;

		}
	
	//read a file from SFTP and save in your application.properties upload.directory respective path
	//this function will create a file according to provided filename and save into provided path in uploadDirectory;
	//upload.directory is that variable which is declare in application.properties and get their values in SFTP Implementation class
	/**
    * this function is used for downloading the file from the sftp server hints:
    * directory : /247tuts/ 
	*fileName : something.extinction 
    */
	@Override
	public void downloadFromSFTP(String SFTPWORKINGDIR, String remoteFile) {
			try {
				JSch jsch = new JSch();
				Session session = jsch.getSession(user, host, port);
				session.setPassword(password);
				session.setConfig("StrictHostKeyChecking", "no");
				System.out.println("Establishing Connection...");
				session.connect();
				System.out.println("Connection established.");
				System.out.println("Creating SFTP Channel.");
				ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
				sftpChannel.connect();
				System.out.println("SFTP Channel created.");
				sftpChannel.cd(SFTPWORKINGDIR);
				FileWriter writer = new FileWriter(uploadDirectory + "/file/" + remoteFile);
				InputStream out = null;
				out = sftpChannel.get(remoteFile);
				BufferedReader br = new BufferedReader(new InputStreamReader(out));
				String strLine = "";
				while ((strLine = br.readLine()) != null) {
					writer.write(strLine);
					writer.write(String.format("%n"));
				}
				session.disconnect();
				sftpChannel.disconnect();
				out.close();
				writer.close();
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	
	
	@Override
	public void uploadOnSFTP(){
		try {
		String SFTPWORKINGDIR1 = "/247tuts/"; // specify your directory name
		FileInputStream fileInputStream = null;
		JSch jsch1 = new JSch();
		Session session1 = jsch1.getSession(user, host, port);
		session1.setPassword(password);
		session1.setConfig("StrictHostKeyChecking", "no");
		System.out.println("Establishing Connection...");
		session1.connect();
		System.out.println("Connection established.");
		System.out.println("Creating SFTP Channel.");
		ChannelSftp sftpChannel1 = (ChannelSftp) session1.openChannel("sftp");
		sftpChannel1.connect();
		System.out.println("SFTP Channel created.");
		sftpChannel1.cd(SFTPWORKINGDIR1);
		File folder = new File(uploadDirectory + "/selected/");
		File[] listOfFiles = folder.listFiles();
		for (File file1 : listOfFiles) {
			if (file1.isFile()) {
				fileInputStream = new FileInputStream(uploadDirectory + "/selected/" + file1.getName());
				sftpChannel1.put(fileInputStream, file1.getName());
				fileInputStream.close();
				file1.delete();
			}
		}
		session1.disconnect();
		sftpChannel1.disconnect();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	// this function is used for deleting the file from the SFTP server
	// directory hint : /247tuts/
	@Override
	public void deleteFile(String SFTPWORKINGDIR, String fileName) {
		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, port);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			System.out.println("Establishing Connection...");
			session.connect();
			System.out.println("Connection established.");
			System.out.println("Creating SFTP Channel.");
			ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
			sftpChannel.connect();
			System.out.println("SFTP Channel created.");
			sftpChannel.cd(SFTPWORKINGDIR);
			sftpChannel.rm(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
