package com.example.SFTPController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.SFTPService.SftpService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value="sftp")
public class SFTPController {
	
	@Autowired
	private SftpService sftpService;
	
	@GetMapping("/download")
	public void download(@RequestParam String  SFTPWORKINGDIR,@RequestParam String remoteFile) {
		sftpService.downloadFromSFTP(SFTPWORKINGDIR, remoteFile);
	}
	
	@ApiOperation("get the list of all files which are present on the provided path")
	@GetMapping("/directory")
	public List<String> directory(@RequestParam String path) {
		return sftpService.getList(path);
	}
	
	@ApiOperation("provide the directory and fileName this will delete the file from the SFTP server")
	@PostMapping("/delete")
	public void deleteFromSftp(@RequestParam String directory,@RequestParam String fileName) {
		sftpService.deleteFile(directory, fileName);
	}
	
	@PostMapping("/upload")
	public void uploadOnSFTP() {
		sftpService.uploadOnSFTP();
	}
	

}
