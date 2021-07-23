package io.github.hejcz;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;

public class GraalBugTest {

    @Test
    void passes() {
        runWithMapWithXEntries(5_000);
    }

    @Test
    void fails() {
        // prepare script
        runWithMapWithXEntries(10_000);
    }

    private void runWithMapWithXEntries(int entriesCount) {
        // prepare script
        StringWriter script = new StringWriter();
        script.append("var map = {");
        for (int i = 0; i < entriesCount; i++) {
            String keyAndValue = String.format("%06d", i);
            script.append("\"").append(keyAndValue).append("\": \"").append(keyAndValue).append("\", ");
        }
        script.append("};");
        script.append("\n");
        script.append("var x = 2 + 2;");

        Context context = Context.newBuilder("js")
                .engine(Engine.newBuilder().build())
                .option("js.strict", "true")
                .option("js.ecmascript-version", "12")
                .build();

        Source source = Source.create("js", script.toString());

        // run script
        for (int i = 0; i < 5_000; i++) {
            if (i % 1_000 == 0) {
                System.out.println(i);
            }
            context.eval(source);
        }
    }

}
