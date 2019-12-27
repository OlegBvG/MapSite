import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SiteMapping extends RecursiveTask<Long> {

    private String url2;

    public  SiteMapping(String url2) {
        this.url2 = url2;
    }

        @Override
        protected Long compute() {
        long sum = 1;
        List<SiteMapping> taskList = new ArrayList<>();
        Connection connect = null;
        Document document = null;

            try {
                connect = Jsoup.connect(url2);
                connect.maxBodySize(0);
                connect.timeout(0);

                document = connect.get();
            } catch (Exception e) {
                System.out.println("Not found site " + url2);
                return Long.valueOf(0);
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        Stream <String> elementsGlobal = document.select(Main.cssQuery).select(Main.noPdf).stream().map(e -> e.attr("href"));
        Stream<String> elementsLocal =  document.select(Main.cssQueryLocal).select(Main.noPdf).stream().map(e -> (e.absUrl("href")));


        List<String> elements = Stream.concat(elementsGlobal, elementsLocal).sorted().distinct().collect(Collectors.toList());

        elements.forEach(u -> {if (!Main.listUrl.contains(u)) {
            Main.listUrl.add(u);
            System.out.println(u);
            //                siteMapping(u);
            SiteMapping task = new SiteMapping(u);
            task.fork();
            taskList.add(task);

        }
        });

            for (SiteMapping task : taskList){
                sum += task.join();
            }

        return  sum;
    }
}
