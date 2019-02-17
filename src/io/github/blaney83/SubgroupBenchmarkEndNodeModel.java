package io.github.blaney83;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;


/**
 * This is the model implementation of SubgroupBenchmarkEnd.
 * The downstream companion to the Subgroup Benchmark Start Node, this node passes through the input table unaltered and additionally outputs a table with execution times for comparisons between repeated executions. 		
 *
 * @author Benjamin Laney
 */
public class SubgroupBenchmarkEndNodeModel extends NodeModel {
    

	static final String CFGKEY_COUNT = "Count";

    /** initial default count value. */
    static final int DEFAULT_COUNT = 100;

    // example value: the models count variable filled from the dialog 
    // and used in the models execution method. The default components of the
    // dialog work with "SettingsModels".
    private final SettingsModelIntegerBounded m_count =
        new SettingsModelIntegerBounded(SubgroupBenchmarkEndNodeModel.CFGKEY_COUNT,
                    SubgroupBenchmarkEndNodeModel.DEFAULT_COUNT,
                    Integer.MIN_VALUE, Integer.MAX_VALUE);
    

    protected SubgroupBenchmarkEndNodeModel() {
        super(1, 2);
    }


    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
        return new BufferedDataTable[]{out};
    }

    @Override
    protected void reset() {

    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {

        return new DataTableSpec[]{null};
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    
        m_count.saveSettingsTo(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        m_count.loadSettingsFrom(settings);

    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {

        m_count.validateSettings(settings);

    }

    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {

    }

    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {

    }

}

