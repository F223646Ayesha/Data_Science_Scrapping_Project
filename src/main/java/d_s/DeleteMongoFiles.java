/*package d_s;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class DeleteMongoFiles {

    public static void main(String[] args) {
        // Connect to MongoDB
        MongoDatabase database = MongoClients.create("mongodb://localhost:27017").getDatabase("NeurIPS");
        
        // Get the collection and GridFS bucket
        MongoCollection<Document> collection = database.getCollection("papers");
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);

        // Find all documents with stored PDFs
        List<ObjectId> fileIdsToDelete = new ArrayList<>();
        for (Document doc : collection.find()) {
            if (doc.containsKey("fileId")) {
                fileIdsToDelete.add(doc.getObjectId("fileId"));
            }
        }

        // Delete the files from GridFS and remove their entries from the collection
        for (ObjectId fileId : fileIdsToDelete) {
            gridFSBucket.delete(fileId); // Delete the file from GridFS
            collection.deleteOne(new Document("fileId", fileId)); // Delete metadata from the collection
            System.out.println("Deleted file with ID: " + fileId);
        }

        System.out.println("All files deleted successfully.");
    }
}*/
