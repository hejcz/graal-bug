package io.github.hejcz;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GraalBugTest {

    @Test
    void shouldCloseContext() throws IOException, InterruptedException {
        for (int i = 0; i < 100; i++) {
            System.out.println("run: " + i);
            Context context = Context.newBuilder()
                    .build();
            String script = "(function() { while(true); })";
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            try {
                executorService.schedule(() -> context.close(true), 20, TimeUnit.MILLISECONDS);
                Value fun = context.eval(Source.newBuilder("js", script, "test").build());
                fun.execute();
            } catch (Exception e) {
                // ignore
            } finally {
                executorService.shutdown();
                executorService.awaitTermination(10, TimeUnit.SECONDS);
            }
        }
    }

}
