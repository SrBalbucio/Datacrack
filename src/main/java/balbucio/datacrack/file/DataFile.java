package balbucio.datacrack.file;

import org.json.JSONObject;

import java.io.*;

public class DataFile {

    private File file;

    public DataFile(File file, String name){
        this.file = file;
        try {
            if (!file.exists()) {
                File folder = new File(file.getParent());
                if (!folder.exists()) {
                    folder.mkdir();
                }
                file.createNewFile();
                save(new JSONObject().put("Name", name).toString());
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public JSONObject load() {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }

            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();
            JSONObject json = new JSONObject(builder.toString());
            return json;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void save(String jsonBytes) {
        try {
            if(file.exists()) {
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(jsonBytes.getBytes());
                outputStream.flush();
                outputStream.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
