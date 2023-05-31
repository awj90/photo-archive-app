package ibf2022.batch2.csf.backend.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import ibf2022.batch2.csf.backend.exceptions.MongoDatabaseException;
import ibf2022.batch2.csf.backend.models.Archive;

@Repository
public class ArchiveRepository {

	@Autowired
	private MongoTemplate mongoTemplate;

	private static final String MONGO_COLLECTION_NAME= "archives";

	//TODO: Task 4
	// You are free to change the parameter and the return type
	// Do not change the method's name
	// Write the native mongo query that you will be using in this method
	//
	// db.archives.insert({
	// 	bundleId: "12345678",
	// 	date: 1685513685,
	// 	title: "Australian Holiday",
	// 	name: "Fred",
	// 	comments: "Lorem ipsum",
	// 	urls: [],
	// });

	public void recordBundle(Archive a) throws MongoDatabaseException {
		try {
			mongoTemplate.insert(a, MONGO_COLLECTION_NAME);
		} catch (Exception ex) {
			throw new MongoDatabaseException(ex.getMessage());
		}
	}

	//TODO: Task 5
	// You are free to change the parameter and the return type
	// Do not change the method's name
	// Write the native mongo query that you will be using in this method
	//
	// db.archives.find(
	// 	{ bundleId: "267b52ec" },
	// 	{ bundleId: 1, date: 1, title: 1, name: 1, comments: 1, urls: 1, _id: 0 }
	// );

	public Optional<Archive> getBundleByBundleId(String bundleId) {
		Query query = Query.query(
			Criteria.where("bundleId").is(bundleId)
			);
		query.fields().exclude("_id").include("bundleId", "date", "title", "name", "comments", "urls");
		List<Document> documents = mongoTemplate.find(query, Document.class, MONGO_COLLECTION_NAME);
		if (documents.size() < 1) {
			return Optional.empty();
		} else {
			List<Archive> archives = documents.stream()
												.map((doc) -> Archive.create(doc))
												.collect(Collectors.toList());
			return Optional.of(archives.get(0));
		}
	}

	//TODO: Task 6
	// You are free to change the parameter and the return type
	// Do not change the method's name
	// Write the native mongo query that you will be using in this method
	//
	// db.archives.find(
	// {},
	// { bundleId: 1, date: 1, title: 1, name: 1, comments: 1, urls: 1, _id: 0 }
	// ).sort({ date: -1, title: 1 });
	public List<Archive> getBundles() {
		Query query = new Query().with(
									Sort.by(Sort.Direction.DESC, "date")
									.and(Sort.by(Sort.Direction.ASC, "title"))
									);
		query.fields().exclude("_id").include("bundleId", "date", "title", "name", "comments", "urls");
		List<Document> documents = mongoTemplate.find(query, Document.class, MONGO_COLLECTION_NAME);
		List<Archive> archives = documents.stream()
												.map((doc) -> Archive.create(doc))
												.collect(Collectors.toList());
		return archives;
	}


}
