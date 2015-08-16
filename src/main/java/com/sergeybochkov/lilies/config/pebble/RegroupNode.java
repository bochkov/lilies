package com.sergeybochkov.lilies.config.pebble;

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.NodeVisitor;
import com.mitchellbosecke.pebble.node.AbstractRenderableNode;
import com.mitchellbosecke.pebble.node.expression.Expression;
import com.mitchellbosecke.pebble.node.expression.FilterInvocationExpression;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class RegroupNode extends AbstractRenderableNode {

    private Expression<?> srcList;
    private String name;
    private FilterInvocationExpression filter;
    private String outName;

    public RegroupNode(Expression<?> srcList, String name, FilterInvocationExpression filter, String outName) {
        this.srcList = srcList;
        this.name = name;
        this.filter = filter;
        this.outName = outName;
    }

    @Override
    public void render(PebbleTemplateImpl tmpl, Writer writer, EvaluationContext ctx) throws PebbleException, IOException {
        List<RegroupList> list = new ArrayList<>();

        Object evaluated = srcList.evaluate(tmpl, ctx);
        List<Object> objList = new ArrayList<>();
        if (evaluated instanceof List)
            for (Object aL : (List) evaluated) objList.add(aL);

        for (Object obj : objList) {
            String grouper = "A";

            boolean founded = false;
            for (RegroupList rl : list) {
                if (rl.getGrouper().equals(grouper)) {
                    rl.addToList(obj);
                    founded = true;
                    break;
                }
            }
            if (!founded) {
                RegroupList rl = new RegroupList();
                rl.setGrouper((String)grouper);
                rl.addToList(obj);
                list.add(rl);
            }
        }
        ctx.put(outName, list);
    }

    @Override
    public void accept(NodeVisitor nodeVisitor) {
    }
}
