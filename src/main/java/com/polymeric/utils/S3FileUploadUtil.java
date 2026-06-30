package com.polymeric.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * AWS S3 文件上传工具类
 */
@Component
public class S3FileUploadUtil {
    
    private static AmazonS3 amazonS3;
    private static String bucketName;
    
    @Autowired
    public void setAmazonS3(AmazonS3 amazonS3) {
        S3FileUploadUtil.amazonS3 = amazonS3;
    }
    
    @Value("${s3.file_bucket}")
    public void setBucketName(String bucketName) {
        S3FileUploadUtil.bucketName = bucketName;
    }
    
    
    public static String fileUpload(MultipartFile file,String type) {
		try {
			// 检查文件是否为空或大小是否超过限制
			if (file.isEmpty() || file.getSize() > 200 * 1024 * 1024) { // 假设最大文件大小为 200MB
				return "文件为空或超过最大限制";
			}
			// 创建临时文件
			File tempFile = File.createTempFile("upload-", file.getOriginalFilename(),
					new File(System.getProperty("java.io.tmpdir")));
			file.transferTo(tempFile); // 将 MultipartFile 保存为 File
			// 构建 S3 中的完整路径（目录+文件名）
			String fileName = type +System.currentTimeMillis()+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			// 创建上传请求
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, tempFile);
			// 执行上传
			amazonS3.putObject(putObjectRequest);
			String fileUrl = amazonS3.getUrl(bucketName, fileName).toString();
			return fileUrl;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
    
    public static String fileUploadHtml(MultipartFile file, String type) {
        try {
            if (file.isEmpty() || file.getSize() > 200 * 1024 * 1024) {
                return "文件为空或超过最大限制";
            }
            
            String fileName = type + System.currentTimeMillis() + 
                file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            
            // 不调用 transferTo，直接使用 InputStream
            try (InputStream inputStream = file.getInputStream()) {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());
                metadata.setContentType(file.getContentType());
                
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream, metadata);
                amazonS3.putObject(putObjectRequest);
            }
            
            return amazonS3.getUrl(bucketName, fileName).toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
	


	@SuppressWarnings("unused")
	private static File createTempFile(MultipartFile file) throws IOException {
		// 创建临时文件
		File tempFile = File.createTempFile("upload-", file.getOriginalFilename(),
				new File(System.getProperty("java.io.tmpdir")));

		// 使用输入流将 MultipartFile 内容写入临时文件
		try (InputStream inputStream = file.getInputStream(); FileOutputStream fos = new FileOutputStream(tempFile)) {
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				fos.write(buffer, 0, bytesRead);
			}
		}
		return tempFile;
	}

	@SuppressWarnings("unused")
	private static String uploadToS3(File file, String fileName) {
		try {
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType("text/html; charset=UTF-8");

			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file);
			putObjectRequest.setMetadata(metadata);

			amazonS3.putObject(putObjectRequest);
			return amazonS3.getUrl(bucketName, fileName).toString();

		} catch (AmazonS3Exception e) {
			e.printStackTrace();
			return null;
		}
	}

    
}