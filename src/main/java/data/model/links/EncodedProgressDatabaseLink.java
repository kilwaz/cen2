package data.model.links;


import data.model.DatabaseLink;
import data.model.objects.EncodedProgress;

import java.util.UUID;

public class EncodedProgressDatabaseLink extends DatabaseLink {
    public EncodedProgressDatabaseLink() {
        super("encoded_progress", EncodedProgress.class);

        // Make sure the order is the same as column order in database
        link("uuid", method("getUuidString"), method("setUuid", UUID.class)); // 1
        link("pass_phase", method("getPassPhase"), method("setPassPhase", Integer.class)); // 2
        link("pass_1_progress", method("getPass1Progress"), method("setPass1Progress", Integer.class)); // 3
        link("pass_1_file_name", method("getPass1FileName"), method("setPass1FileName", String.class)); // 4
        link("pass_2_progress", method("getPass2Progress"), method("setPass2Progress", Integer.class)); // 5
        link("pass_2_file_name", method("getPass2FileName"), method("setPass2FileName", String.class)); // 6
    }
}



