import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

/**
 * This example demonstrates a fully asynchronous execution of multiple HTTP exchanges
 * where the result of an individual operation is reported using a callback interface.
 */
public class HttpTestClientMain {

    public static void main(final String[] args) throws Exception {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(3000)
                .setConnectTimeout(3000)
                .build();

        CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setMaxConnTotal(2)
                .build();

        int queries = 100000;
        int batch_size = 100;
        int runs = queries / batch_size;


        httpclient.start();

        for (int i = 0; i < runs; i++) {
            execute(httpclient, batch_size);
        }

        httpclient.close();
        System.out.println("Done");
    }

    private static void execute(CloseableHttpAsyncClient httpclient, int batchSize) throws InterruptedException {

        List<HttpGet> requests = new ArrayList<>();
        IntStream.iterate(0, i -> i + 1)
                .limit(batchSize)
                .mapToObj(x ->
                                new HttpGet("http://localhost:8098/buckets/netic/index/group_bin/a/b")
                ).forEach(x -> requests.add(x));

        final CountDownLatch latch = new CountDownLatch(requests.size());
        for (final HttpGet request : requests) {
            httpclient.execute(request, new FutureCallback<HttpResponse>() {

                public void completed(final HttpResponse response) {
                    latch.countDown();
                    //   System.out.println(request.getRequestLine() + "->" + response.getStatusLine());
                }

                public void failed(final Exception ex) {
                    latch.countDown();
                    // System.out.println(request.getRequestLine() + "->" + ex);
                }

                public void cancelled() {
                    latch.countDown();
                    System.out.println(request.getRequestLine() + " cancelled");
                }

            });
        }
        latch.await();
        // System.out.println("next batch");


    }

}