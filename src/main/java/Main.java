import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

public class Main {

    private static String inHtml = "https://skillbox.ru/";
    public static String cssQuery = "a[href~=^(https://skillbox.ru)/]";
    public static String cssQueryLocal = "a[href~=^(/).+/]";
    public static String noPdf = "a[href$=/]";

    public static HashSet listUrl = new HashSet();

    public static void main(String[] args) throws IOException, InterruptedException {

        listUrl.add(inHtml);
        long sum = new ForkJoinPool().invoke(new SiteMapping(inHtml));

        System.out.println("Всего сайтов:  " + sum);
        System.out.println("---------------");

        try(FileWriter writer = new FileWriter("map_site.txt", false))
        {
            listUrl.stream().sorted().forEach(ur -> {
                try {
                    writer.write(positionUrl((String) ur));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    static String positionUrl(String url) {
        int countSlash  = (int) url.chars().filter(ch -> ch == '/').count();
        return "\t".repeat(countSlash - 3) + url + "\n";
    }

}

