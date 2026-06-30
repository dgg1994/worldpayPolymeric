package com.polymeric.utils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
@Slf4j
public class FileUploadUtil {
	
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString().replaceAll("-", "");
		return id;
	}

	public static boolean imgLocation(File file) throws IOException {
		String reg = "(mp4|flv|avi|rm|rmvb|wmv)";
		Pattern p = Pattern.compile(reg);
		boolean boo = p.matcher(file.getName()).find();
		return boo;
	}

	/**
	 * @category 获取文件大小
	 * @param mulfile
	 * @return
	 */
	public static String GetFileSize(MultipartFile mulfile) {
		String size = "";
		long fileS = mulfile.getSize();
		DecimalFormat df = new DecimalFormat("#.00");
		if (fileS < 1024) {
			size = df.format((double) fileS) + "BT";
		} else if (fileS < 1048576) {
			size = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			size = df.format((double) fileS / 1048576) + "MB";
		} else {
			size = df.format((double) fileS / 1073741824) + "GB";
		}
		return size;
	}

	/**
	 * @category 获取文件大小
	 * @param file
	 * @return
	 */
	public static String findFileSize(File file) {
		String size = "";
		if (file.exists() && file.isFile()) {
			long fileS = file.length();
			DecimalFormat df = new DecimalFormat("#.00");
			if (fileS < 1024) {
				size = df.format((double) fileS) + "BT";
			} else if (fileS < 1048576) {
				size = df.format((double) fileS / 1024) + "KB";
			} else if (fileS < 1073741824) {
				size = df.format((double) fileS / 1048576) + "MB";
			} else {
				size = df.format((double) fileS / 1073741824) + "GB";
			}
		} else if (file.exists() && file.isDirectory()) {
			size = "";
		} else {
			size = "0BT";
		}
		return size;
	}


	/**
	 * 将文件转换成byte数组
	 * 
	 * // * @param filePath
	 * 
	 * @return
	 */
	public static byte[] File2byte(File tradeFile) {
		byte[] buffer = null;
		try {
			FileInputStream fis = new FileInputStream(tradeFile);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	public static boolean isImage(MultipartFile file) {
		String contentType = file.getContentType();
		return contentType != null && contentType.startsWith("image/");
	}

    
	public static File createTempFileFromBytes(byte[] bytes, String originalFilename) throws IOException {
	    File tempFile = File.createTempFile("upload_", "_" + originalFilename);
	    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
	        fos.write(bytes);
	    }
	    return tempFile;
	}
    
   
}
