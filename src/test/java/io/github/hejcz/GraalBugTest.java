package io.github.hejcz;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GraalBugTest {

    @Test
    void shouldHandleRegex() throws IOException, InterruptedException {
        Context context = Context.newBuilder().build();
        String script = "(function abc() { 'some test text'.match('tes') })();";
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        try {
            executorService.schedule(() -> context.close(true), 3, TimeUnit.SECONDS);
            context.eval(Source.newBuilder("js", script, "test").build());
        } finally {
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        }
    }

}
