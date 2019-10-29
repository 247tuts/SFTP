package com.example.SFTPService;

import java.util.List;



public interface SftpService {
	
	public List<String> getList(String path);
	
	public void downloadFromSFTP(String SFTPWORKINGDIR, String remoteFile);
	
	public void uploadOnSFTP();
	
	public void deleteFile(String SFTPWORKINGDIR, String fileName);
	
	
	
		
	
	
	
	
	
	
	
	
	
}
