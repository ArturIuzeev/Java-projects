package info.kgeorgiy.ja.iuzeev.crawler;

import info.kgeorgiy.java.advanced.crawler.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.*;

public class WebCrawler implements AdvancedCrawler {

    private final int perHost;
    private final Downloader downloader;
    private final ExecutorService downloaders;
    private final ExecutorService extractors;
    private final Map<String, Page> pageMap;
    private List<String> hosts;

    public WebCrawler(Downloader downloader, int downloaders, int extractors, int perHost) {
        this.downloader = downloader;
        this.downloaders = Executors.newFixedThreadPool(downloaders);
        this.extractors = Executors.newFixedThreadPool(extractors);
        this.perHost = perHost;
        this.pageMap = new ConcurrentHashMap<>();
        this.hosts = null;
    }

    @Override
    public Result download(String url, int depth, List<String> hosts) {
        this.hosts = hosts;
        return download(url, depth);
    }

    @Override
    public Result download(String url, int depth) {
        Set<String> usedLinks = ConcurrentHashMap.newKeySet();
        Set<String> result = ConcurrentHashMap.newKeySet();
        Map<String, IOException> errors = new ConcurrentHashMap<>();
        Queue<String> queue = new ConcurrentLinkedQueue<>();

        queue.add(url);

        Phaser phaser = new Phaser(1);
        while (phaser.getPhase() < depth && !queue.isEmpty()) {
            Set<String> links = ConcurrentHashMap.newKeySet();

            while (!queue.isEmpty()) {
                String u = queue.poll();
                if (usedLinks.add(u)) {
                    downloadPage(u, result, depth - phaser.getPhase(), errors, links, phaser);
                }
            }

            phaser.arriveAndAwaitAdvance();
            queue.addAll(links);
        }
        return new Result(new ArrayList<>(result), errors);
    }

    private void downloadPage(String url, Set<String> result, int depth, Map<String, IOException> errors, Set<String> links, Phaser phaser) {
        String host;
        try {
            host = URLUtils.getHost(url);
            if (hosts != null && !hosts.contains(host)) {
                return;
            }
        } catch (MalformedURLException e) {
            errors.put(url, e);
            return;
        }

        Page page;
        if (pageMap.containsKey(host)) {
            page = pageMap.get(host);
        } else {
            page = new Page(downloaders, perHost);
            pageMap.put(host, page);
        }

        phaser.register();

        page.addTask(() -> {
            try {
                Document document = downloader.download(url);
                result.add(url);

                if (depth != 0) {
                    phaser.register();
                    extractors.submit(() -> {
                        try {
                            links.addAll(document.extractLinks());
                        } catch (IOException e) {
                            errors.put(url, e);
                        } finally {
                            phaser.arriveAndDeregister();
                        }
                    });
                }
            } catch (IOException e) {
                errors.put(url, e);
            } finally {
                phaser.arriveAndDeregister();
                page.next();
            }
        });
    }

    @Override
    public void close() {
        downloaders.shutdownNow();
        extractors.shutdownNow();
    }

    public static void main(String[] args) {
        if (args == null || args.length > 5 || args.length < 1) {
            System.err.println("Expected: WebCrawler url [depth [downloads [extractors [perHost]]]]");
            return;
        }

        String url = args[0];
        int depth = getArg(1, args);
        int downloads = getArg(2, args);
        int extractors = getArg(3, args);
        int perHost = getArg(4, args);

        WebCrawler webCrawler;
        try {
            webCrawler = new WebCrawler(new CachingDownloader(100), downloads, extractors, perHost);
            webCrawler.download(url, depth);
            webCrawler.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int getArg(int i, String[] args) {
        return i >= args.length ? 2 : Integer.parseInt(args[i]);
    }
}