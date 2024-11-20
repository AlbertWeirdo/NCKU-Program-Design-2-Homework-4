import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class test {
    public static void main(String[] args) {
        HashMap<Integer, HashMap<String, Integer>> docs = new HashMap<Integer, HashMap<String, Integer>>();
        try (BufferedReader br = new BufferedReader(new FileReader("/home/albert/hw4/docs.txt"))) {
            RefindDoc rd = new RefindDoc();
            String currentLine = br.readLine();
            while (currentLine != null) {
                currentLine = rd.refine(currentLine);
                for (int i = 0; i < 5; i++) {
                    if (i < 4) {
                        currentLine += rd.refine(br.readLine());
                    } else {
                        currentLine += rd.refine(br.readLine());
                    }
                }

                currentLine = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class RefindDoc {
    public String refine(String line) {
        return line;
    }
}
