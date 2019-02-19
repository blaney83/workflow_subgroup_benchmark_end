package io.github.blaney83;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "SubgroupBenchmarkEnd" Node.
 * The downstream companion to the Subgroup Benchmark Start Node, this node passes through the input table unaltered and additionally outputs a table with execution times for comparisons between repeated executions. 		
 *
 * @author Benjamin Laney
 */
public class SubgroupBenchmarkEndNodeFactory 
        extends NodeFactory<SubgroupBenchmarkEndNodeModel> {

    @Override
    public SubgroupBenchmarkEndNodeModel createNodeModel() {
        return new SubgroupBenchmarkEndNodeModel();
    }

    @Override
    public int getNrNodeViews() {
        return 0;
    }

    @Override
    public NodeView<SubgroupBenchmarkEndNodeModel> createNodeView(final int viewIndex,
            final SubgroupBenchmarkEndNodeModel nodeModel) {
        return null;
    }

    @Override
    public boolean hasDialog() {
        return false;
    }

    @Override
    public NodeDialogPane createNodeDialogPane() {
        return null;
    }

}

