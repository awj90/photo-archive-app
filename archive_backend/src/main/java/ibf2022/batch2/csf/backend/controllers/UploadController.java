package ibf2022.batch2.csf.backend.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;

import ibf2022.batch2.csf.backend.exceptions.MongoDatabaseException;
import ibf2022.batch2.csf.backend.models.Archive;
import ibf2022.batch2.csf.backend.services.UploadService;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;

@Controller
@RequestMapping
@CrossOrigin(origins="*")
public class UploadController {

	@Autowired
	private UploadService uploadService;

	// TODO: Task 2, Task 3, Task 4
	@PostMapping(path="/upload", consumes=MediaType.MULTIPART_FORM_DATA_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> uploadHandler(@RequestPart String name, @RequestPart String title, @RequestPart String comments, @RequestPart MultipartFile archive){
		try {
			List<String> urls = uploadService.upload(name, title, comments, archive);
			Archive a = new Archive();
			String bundleId = UUID.randomUUID().toString().substring(0, 8);
			a.setBundleId(bundleId);
			a.setDate(new Date().getTime());
			a.setTitle(title);
			a.setName(name);
			a.setComments(comments);
			a.setUrls(urls);
			uploadService.recordBundle(a);
			return ResponseEntity.status(HttpStatus.CREATED)
								.contentType(MediaType.APPLICATION_JSON)
								.body(Json.createObjectBuilder()
											.add("bundleId", bundleId)
											.build()
											.toString());
		} catch (IOException | SdkClientException | MongoDatabaseException ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
								.contentType(MediaType.APPLICATION_JSON)
								.body(Json.createObjectBuilder()
											.add("error", ex.getMessage())
											.build()
											.toString());
		}
	}


	// TODO: Task 5
	@GetMapping(path="/bundle/{bundleId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getBundleById(@PathVariable String bundleId) {
		Optional<Archive> archive = uploadService.getBundleByBundleId(bundleId);
		
		if (archive.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
								.contentType(MediaType.APPLICATION_JSON)
								.body(Json.createObjectBuilder()
											.add("error", "bundleId %s not found".formatted(bundleId))
											.build()
											.toString());
		}

		return ResponseEntity.status(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(archive.get().toJsonObjectBuilder().build().toString());
	}



	// TODO: Task 6
	@GetMapping(path="/bundles", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getBundles() {
		List<Archive> archives = uploadService.getBundles();
		if (archives.size() < 1) {
			return ResponseEntity.status(HttpStatus.OK)
								.contentType(MediaType.APPLICATION_JSON)
								.body(Json.createArrayBuilder().build().toString());
		} else {
			JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
			for (Archive archive: archives) {
				jsonArrayBuilder.add(archive.toJsonObjectBuilder());
			}
			return ResponseEntity.status(HttpStatus.OK)
								.contentType(MediaType.APPLICATION_JSON)
								.body(jsonArrayBuilder.build().toString());
		}
	}
}
