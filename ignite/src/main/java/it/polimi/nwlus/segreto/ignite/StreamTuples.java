package it.polimi.nwlus.segreto.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

/**
 * Created by affo on 06/09/16.
 */
public class StreamTuples {
    public static void main(String[] args) throws Exception {
        int windowSize = Integer.parseInt(args[0]);

        try (Ignite ignite = Ignition.start()) {
            IgniteCache<String, String> stmCache = ignite.getOrCreateCache(CacheConfig.tupleCache(windowSize));

            // Create a streamer to stream words into the cache.
            try (IgniteDataStreamer<String, String> stmr = ignite.dataStreamer(stmCache.getName())) {
                // Allow data updates.
                stmr.allowOverwrite(true);

                ServerSocket srv = new ServerSocket(9999);
                Socket s = srv.accept();
                BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream()));

                String msg;
                do {
                    msg = r.readLine();
                    // every tuple has a unique key
                    stmr.addData(UUID.randomUUID().toString(), msg);
                    stmr.flush();
                    System.out.println(">>> " + msg);
                } while (msg != null);
            }
        }
    }
}
