package io.github.blaney83;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataTableSpecCreator;
import org.knime.core.data.RowIterator;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.time.localdatetime.LocalDateTimeCellFactory;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.time.util.DateTimeUtils;

/**
 * This is the model implementation of SubgroupBenchmarkEnd. The downstream
 * companion to the Subgroup Benchmark Start Node, this node passes through the
 * input table unaltered and additionally outputs a table with execution times
 * for comparisons between repeated executions.
 *
 * @author Benjamin Laney
 */
public class SubgroupBenchmarkEndNodeModel extends NodeModel {

	private static final int IN_PORT = 0;

//	private static final int DATA_TABLE_OUT_PORT = 0;
//	private static final int TIME_TABLE_OUT_PORT = 1;
//
//	private static int m_runCount = 1;

	private static final String TIME_TABLE_NAME = "Execution Times";

	// note config keys must match with the "Start" node keys!
	private static final String CFGKEY_RUN_NAME = "runName";
	private static final String CFGKEY_CLEAR_DATA = "clearData";
	private static final String CFGKEY_RUN_COUNT = "runCount";
	private static final String CFGKEY_START_TIME = "runStartTime";
	private static final String CFGKEY_RUN_DATE = "runDate";
	private static final String CFGKEY_RUN_NOTES = "runNotes";
	
	private List<DataRow> m_timeInfoList;

	protected SubgroupBenchmarkEndNodeModel() {
		super(1, 2);
	}

	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		m_timeInfoList = new LinkedList<DataRow>();
		Map<String, String> infoProperties = inData[IN_PORT].getSpec().getProperties();
		DataTable timeTable = createTimeTable(infoProperties, outputSpecBuilder());
		BufferedDataTable finalTimeTable = exec.createBufferedDataTable(timeTable, exec);
		return new BufferedDataTable[] { inData[IN_PORT], finalTimeTable };
	}

	private class TimeTable implements DataTable {

		private final DataTableSpec timeTableSpec;
		private final Map<RowKey, DataRow> timeTableRows;

		public TimeTable(final DataTableSpec spec
//				,final Map<RowKey, DataRow> rows
				) {
			this.timeTableSpec = spec;
			this.timeTableRows = new LinkedHashMap<RowKey, DataRow>();
		}

		@Override
		public DataTableSpec getDataTableSpec() {
			return this.timeTableSpec;
		}

		@Override
		public RowIterator iterator() {
			return new RowIterator() {
				
				@Override
				public DataRow next() {
					Iterator<Map.Entry<RowKey, DataRow>> entries = timeTableRows.entrySet().iterator();
					while (entries.hasNext()) {
						return entries.next().getValue();
					}
					return null;
				}
				
				@Override
				public boolean hasNext() {
					Iterator<Map.Entry<RowKey, DataRow>> entries = timeTableRows.entrySet().iterator();
					while (entries.hasNext()) {
						return true;
					}
					return false;
				}
			};
		}
		
		public void addRow(final RowKey key, final DataRow row) {
			timeTableRows.getOrDefault(key, row);
		}

	}

	private DataTable createTimeTable(final Map<String, String> infoProperties, final DataTableSpec tableSpecs) {
		Long startTime = Long.valueOf(infoProperties.get(CFGKEY_START_TIME));
		StringBuilder rowKeyBuilder = new StringBuilder(infoProperties.get(CFGKEY_RUN_NAME) + " ");
		rowKeyBuilder.append(infoProperties.get(CFGKEY_RUN_COUNT));
		RowKey newRowKey = new RowKey(rowKeyBuilder.toString());
		Long estimatedElapsedTime = System.nanoTime() - startTime;
		String notes = infoProperties.get(CFGKEY_RUN_NOTES);
		DataTableSpecCreator newTableSpecs = new DataTableSpecCreator();
		String date = infoProperties.get(CFGKEY_RUN_DATE);
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		DataCell[] newCells = new DataCell[3];
		newCells[0] = new DoubleCell(estimatedElapsedTime.doubleValue());
		newCells[1] = new LocalDateTimeCellFactory().createCell(date);
//		newCells[1] = new LocalDateTimeCellFactory().createCell(dateFormat.format(Date.valueOf(date)));
		newCells[2] = new StringCell(notes);
		DefaultRow newRow = new DefaultRow(newRowKey, newCells);
		m_timeInfoList.add(newRow);
		TimeTable timeTable = new TimeTable(newTableSpecs.createSpec());
		for(DataRow row: m_timeInfoList) {
			timeTable.addRow(row.getKey(), row);
		}
		return timeTable;
	}
//
//	private class TimeCellFactory implements CellFactory {
//
//		private final List<Double> m_timeList;
//		private final List<LocalDateTime> m_dateList;
//		private final List<String> m_noteList;
//		private final DataTableSpec m_tableSpec;
//
//		public TimeCellFactory(final DataTableSpec tableSpec, final List<Double> timeList,
//				final List<LocalDateTime> dateList, final List<String> noteList) {
//			this.m_tableSpec = tableSpec;
//			this.m_timeList = timeList;
//			this.m_dateList = dateList;
//			this.m_noteList = noteList;
//		}
//
//		@Override
//		public DataCell[] getCells(DataRow row) {
//			DataCell[] outGoingCells = new DataCell[row.getNumCells()];
//			int count = 0;
//			for(DataColumnSpec colSpec : m_tableSpec) {
//				DataCell newCell;
//				switch(count) {
//				case(0):
//					newCell = new DoubleCell(estimatedElapsedTime.doubleValue());
//					break;
//				case(1):
//					newCell = new DateAndTimeCell(date.getYear(), date.getMonth(), date.getDayOfMonth(), date.getHour(), date.getMinute(), date.getSecond());
//					break;
//				case(2):
//					newCell = new StringCell(notes[]);
//					break;
//				}
//				count ++;
//			}
//			return null;
//		}
//
//		@Override
//		public DataColumnSpec[] getColumnSpecs() {
//			List<DataColumnSpec> outGoingColSpecs = new LinkedList<DataColumnSpec>();
//			for (DataColumnSpec colSpec : m_tableSpec) {
//				outGoingColSpecs.add(colSpec);
//			}
//			return outGoingColSpecs.toArray(new DataColumnSpec[outGoingColSpecs.size()]);
//		}
//
//		@Override
//		public void setProgress(int curRowNr, int rowCount, RowKey lastKey, ExecutionMonitor exec) {
//
//		}
//
//	}

//	private Map<String, String> propertiesBuilder() {
//		Map<String, String> properties = new LinkedHashMap<String, String>();
//		properties.put(CFGKEY_START_TIME, Long.toString(System.nanoTime()));
//		properties.put(CFGKEY_RUN_COUNT, Integer.toString(m_runCount));
//		properties.put(CFGKEY_RUN_NAME, m_runName.getStringValue());
//		properties.put(CFGKEY_CLEAR_DATA, Boolean.toString(m_clearData.getBooleanValue()));
//		return properties;
//	}

	@Override
	protected void reset() {
		//will have to save and clear row list
	}

	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		DataTableSpec newOutSpecs = null;
		boolean isCompatibleTable = false;
		if (inSpecs[IN_PORT] == null || inSpecs[IN_PORT].getProperties() == null) {
			Map<String, String> infoProperties = inSpecs[IN_PORT].getProperties();
			if (infoProperties.containsKey(CFGKEY_RUN_COUNT) && infoProperties.containsKey(CFGKEY_RUN_NAME)
					&& infoProperties.containsKey(CFGKEY_START_TIME) && infoProperties.containsKey(CFGKEY_CLEAR_DATA)
					&& infoProperties.containsKey(CFGKEY_RUN_DATE) && infoProperties.containsKey(CFGKEY_RUN_NOTES)) {
				isCompatibleTable = true;
//				m_clearData.setBooleanValue();
				if (Boolean.valueOf(infoProperties.get(CFGKEY_CLEAR_DATA))) {
					// clear cached data
				}
			}
			if (!isCompatibleTable) {
				throw new InvalidSettingsException(
						"Please ensure that the data table provided at this in port has been passed through the 'Start' node.");
			}
			DataTableSpecCreator secondTableSpecCreator = new DataTableSpecCreator();
			secondTableSpecCreator.setName(TIME_TABLE_NAME);
			secondTableSpecCreator.addColumns(outputSpecBuilder());
			newOutSpecs = secondTableSpecCreator.createSpec();
		}

		return new DataTableSpec[] { inSpecs[IN_PORT], newOutSpecs };
	}

	public DataTableSpec outputSpecBuilder() {
		DataColumnSpecCreator timeSpec = new DataColumnSpecCreator("Time", new DoubleCell(0.0).getType());
		DataColumnSpecCreator dateSpec = new DataColumnSpecCreator("Execution Date",
				new LocalDateTimeCellFactory().getDataType());
		DataColumnSpecCreator notesSpec = new DataColumnSpecCreator("Notes", new StringCell("").getType());
		DataColumnSpec[] colSpecArr = new DataColumnSpec[] { timeSpec.createSpec(), dateSpec.createSpec(),
				notesSpec.createSpec() };
		DataTableSpecCreator secondTableSpecCreator = new DataTableSpecCreator();
		secondTableSpecCreator.setName(TIME_TABLE_NAME);
		return secondTableSpecCreator.createSpec();
	}

	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		// not needed at this time
	}

	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		// not needed at this time
	}

	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		// not needed at this time
	}

	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// not needed at this time
	}

	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// not needed at this time
	}

	
}
