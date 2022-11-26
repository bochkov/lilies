package sb.lilies.cfg.pebble;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.pebbletemplates.pebble.error.PebbleException;
import io.pebbletemplates.pebble.extension.NodeVisitor;
import io.pebbletemplates.pebble.node.AbstractRenderableNode;
import io.pebbletemplates.pebble.node.expression.*;
import io.pebbletemplates.pebble.template.EvaluationContextImpl;
import io.pebbletemplates.pebble.template.PebbleTemplateImpl;
import lombok.RequiredArgsConstructor;

public final class RegroupNode extends AbstractRenderableNode {

    private final Expression<?> source;
    private final String field;
    private final List<FilterInvocationExpression> filters;
    private final String outName;
    private final String filename;
    private final int lineNumber;

    public RegroupNode(Expression<?> source, String field, List<FilterInvocationExpression> filters,
                       String outName, String filename, int lineNumber) {
        this.source = source;
        this.field = field;
        this.filters = filters;
        this.outName = outName;
        this.filename = filename;
        this.lineNumber = lineNumber;
    }

    @Override
    public void render(PebbleTemplateImpl tmpl, Writer writer, EvaluationContextImpl ctx) throws IOException {
        Map<String, List<Object>> map = new TreeMap<>();

        List<?> list = (List<?>) this.source.evaluate(tmpl, ctx);
        for (Object object : list) {
            map.computeIfAbsent(
                    new FilteredExpression(filters,
                            new GetAttributeExpression(
                                    new ObjectExpression(object, lineNumber),
                                    new LiteralStringExpression(field, lineNumber),
                                    null, filename, lineNumber))
                            .evaluate(tmpl, ctx)
                            .toString(),
                    v -> new ArrayList<>()
            ).add(object);
        }
        ctx.getScopeChain().put(outName, map);
    }

    @Override
    public void accept(NodeVisitor nodeVisitor) {
        //
    }

    @RequiredArgsConstructor
    private static final class ObjectExpression implements Expression<Object> {

        private final Object object;
        private final int lineNumber;

        @Override
        public Object evaluate(PebbleTemplateImpl self, EvaluationContextImpl context) {
            return object;
        }

        @Override
        public int getLineNumber() {
            return lineNumber;
        }

        @Override
        public void accept(NodeVisitor visitor) {
            //
        }
    }

    @RequiredArgsConstructor
    private static final class FilteredExpression implements Expression<Object> {

        private final List<FilterInvocationExpression> filters;
        private final Expression<Object> origin;

        @Override
        public Object evaluate(PebbleTemplateImpl self, EvaluationContextImpl context) throws PebbleException {
            Expression<?> last = origin;
            for (FilterInvocationExpression filter : filters) {
                FilterExpression fexp = new FilterExpression();
                fexp.setRight(filter);
                fexp.setLeft(last);
                last = fexp;
            }
            return last.evaluate(self, context);
        }

        @Override
        public int getLineNumber() {
            return origin.getLineNumber();
        }

        @Override
        public void accept(NodeVisitor visitor) {
            //
        }
    }
}
