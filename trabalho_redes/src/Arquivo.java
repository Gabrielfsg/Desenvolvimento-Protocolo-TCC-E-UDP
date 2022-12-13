import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Arquivo {

    public static void escreveArq(String caminho, String upload, String download) throws IOException{
        Path path = Paths.get(caminho);
        File file = new File(path.toAbsolutePath().toString());
        FileWriter fw = null;
        if ( file.exists()) {
            file.delete();
            fw = new FileWriter( caminho );
        }
        BufferedWriter buffWrite = new BufferedWriter(fw);
        buffWrite.append(upload+"#"+download);
        buffWrite.close();
    }

    public static ArrayList<String> lerArq(String caminho) throws IOException {
        Path path = Paths.get(caminho);
        String texto = Files.readString(path, StandardCharsets.UTF_8);
        return new ArrayList<String>(Arrays.asList(texto.split("#")));
    }
}
