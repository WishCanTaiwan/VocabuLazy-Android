package wishcantw.vocabulazy.database;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import wishcantw.vocabulazy.database.object.Note;
import wishcantw.vocabulazy.database.object.OptionSettings;

public class FileWriter {

    // the singleton
    private static FileWriter fileWriter = new FileWriter();

    // make constructor private to prevent from instantiating
    private FileWriter() {}

    // the getter of the singleton
    public static synchronized FileWriter getInstance() {
        return fileWriter;
    }

    /**
     * Write note to file
     *
     * @param context the context
     * @param notes the array list of notes
     */
    public void writeNote(@NonNull Context context,
                          @NonNull ArrayList<Note> notes) {
        write(context, "note", notes.toArray());
    }

    /**
     * Write option settings to file
     *
     * @param context the context
     * @param optionSettings the array list of option settings
     */
    public void writeOptionSettings(@NonNull Context context,
                                    @NonNull ArrayList<OptionSettings> optionSettings) {
        write(context, "optionSetting", optionSettings.toArray());
    }

    private <T> void write(Context context, String filename, T[] array) {
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(new Gson().toJson(array).getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
