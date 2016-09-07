package it.polimi.nwlus.segreto.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;

import java.util.List;

/**
 * Created by affo on 06/09/16.
 */
public class QueryWindow {
    public static void main(String[] args) throws Exception {
        // Mark this cluster member as client.
        Ignition.setClientMode(true);
        int windowSlide = Integer.parseInt(args[0]);

        try (Ignite ignite = Ignition.start()) {
            IgniteCache<String, String> stmCache = ignite.getOrCreateCache(CacheConfig.tupleCache(5));

            // Select top 10 words.
            SqlFieldsQuery window = new SqlFieldsQuery(
                    "select _val from String");

            System.out.println("The slide is " + windowSlide);
            System.out.println();

            int i = 0;
            while (true) {
                List<List<?>> windowContent;
                try {
                    // Execute queries.
                    windowContent = stmCache.query(window).getAll();
                } catch (Exception e) {
                    // something went wrong.
                    // The experiment is over.
                    break;
                }

                System.out.print("[");
                for (List<?> el : windowContent) {
                    System.out.print(el.get(0));
                    System.out.print(" ");
                }
                System.out.print("] - " + i);

                if (!windowContent.isEmpty()) {
                    System.out.println();
                } else {
                    System.out.print(", ");
                }

                Thread.sleep(windowSlide * 1000L);
                i++;
            }
        }
    }
}