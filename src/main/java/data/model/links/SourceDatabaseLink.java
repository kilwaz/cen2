package data.model.links;


import data.model.DatabaseLink;
import data.model.objects.EncodedProgress;
import data.model.objects.Source;
import org.json.JSONObject;

import java.util.UUID;

public class SourceDatabaseLink extends DatabaseLink {
    public SourceDatabaseLink() {
        super("source", Source.class);

        // Make sure the order is the same as column order in database
        link("uuid", method("getUuidString"), method("setUuid", UUID.class)); // 1
        link("file_name", method("getFileName"), method("setFileName", String.class)); // 2
        link("encoded_file_name", method("getEncodedFileName"), method("setEncodedFileName", String.class)); // 3
        link("url", method("getUrl"), method("setUrl", String.class)); // 4
        link("encoded_url", method("getEncodedUrl"), method("setEncodedUrl", String.class)); // 5
        link("name", method("getName"), method("setName", String.class)); // 6
        link("encoded_progress_id", method("getEncodedProgressUUID"), method("setEncodedProgress", EncodedProgress.class)); // 7
        link("source_info_json", method("getSourceInfo"), method("setSourceInfo", JSONObject.class)); // 8
    }
}
