package ibf2022.batch2.csf.backend.repositories;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

@Repository
public class ImageRepository {

	@Autowired
	private AmazonS3 s3;

	//TODO: Task 3
	// You are free to change the parameter and the return type
	// Do not change the method's name
	public URL upload(String name, String title, String comments, File file) throws IOException, AmazonServiceException, SdkClientException {

		// Add custom metadata
		Map<String, String> userData = new HashMap<>();
		userData.put("name", name);
		userData.put("title", title);
		userData.put("comments", comments);
		userData.put("original-file-name", file.getName());
		userData.put("upload-date", (new Date()).toString());
  
		// Add object's metadata 
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(getContentType(file.getName()));
		metadata.setContentLength(file.length());
		metadata.setUserMetadata(userData);
  
		// Generate a unique key name
		String key = UUID.randomUUID().toString().substring(0, 8);
  
		// arg0: awj90 - bucket name
		// arg1: key - key
		// arg2: input stream
		// arg3: ObjectMetadata
		PutObjectRequest putReq = new PutObjectRequest("awj90", key, 
			  new FileInputStream(file), metadata);
		// Make the file publically accessible
		putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);
  
		PutObjectResult result = s3.putObject(putReq);
		System.out.printf(">>> result: %s\n", result);
  
		return s3.getUrl("awj90", key);
	 }
	
	 private String getContentType(String fileName) {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>");
		System.out.println(Arrays.toString(fileName.split(".")));
		return "image/" + fileName.split("\\.")[1];
	  }
}
