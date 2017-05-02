package sb.lilies.pebble;

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.NodeVisitor;
import com.mitchellbosecke.pebble.node.AbstractRenderableNode;
import com.mitchellbosecke.pebble.node.expression.*;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class RegroupNode extends AbstractRenderableNode {

    private final Expression source;
    private final String field;
    private final List<FilterInvocationExpression> filters;
    private final String outName;
    private final String filename;
    private final int lineNumber;

    public RegroupNode(Expression source, String field, List<FilterInvocationExpression> filters,
                       String outName, String filename, int lineNumber) {
        this.source = source;
        this.field = field;
        this.filters = filters;
        this.outName = outName;
        this.filename = filename;
        this.lineNumber = lineNumber;
    }

    @Override
    public void render(PebbleTemplateImpl tmpl, Writer writer, EvaluationContext ctx) throws PebbleException, IOException {
        Map<String, List<Object>> map = new TreeMap<>();

        List list = (List) this.source.evaluate(tmpl, ctx);
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
    }

    private class ObjectExpression implements Expression {

        private final Object object;
        private final int lineNumber;

        public ObjectExpression(Object object, int lineNumber) {
            this.object = object;
            this.lineNumber = lineNumber;
        }

        @Override
        public Object evaluate(PebbleTemplateImpl self, EvaluationContext context) throws PebbleException {
            return object;
        }

        @Override
        public int getLineNumber() {
            return lineNumber;
        }

        @Override
        public void accept(NodeVisitor visitor) {
        }
    }

    private class FilteredExpression implements Expression {

        private final Expression origin;
        private final List<FilterInvocationExpression> filters;

        public FilteredExpression(List<FilterInvocationExpression> filters, Expression origin) {
            this.origin = origin;
            this.filters = filters;
        }

        @Override
        public Object evaluate(PebbleTemplateImpl self, EvaluationContext context) throws PebbleException {
            Expression last = origin;
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

        }
    }
}
