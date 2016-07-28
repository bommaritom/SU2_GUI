package gui;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import io.History;
import io.SurfaceFlow;

/**
 * This class is designed to easily create multiple panels which
 * can graph various properties of surface_flow.csv
 *
 *
 */
public class GraphPanels{
	
	private SurfaceFlow surfaceFlow;
	private History history;
	
	public Map<String, JPanel> graphs;
	public JPanel sfGraph;
    
	
	public GraphPanels() throws FileNotFoundException{
		
		surfaceFlow = new SurfaceFlow("su2/surface_flow.csv");
		history	= new History("su2/history.dat");
		
		//create desired graphs
		JPanel surfacePressurePanel = createSurfacePressurePanel();

		//add the graphs
		graphs = new HashMap<String, JPanel>();
		graphs.put("Surface Pressure", surfacePressurePanel);
		sfGraph = surfacePressurePanel;
		
	}
	
	
	
	private ChartPanel createSurfacePressurePanel(){
		
		//surface pressure subplot
		final XYSeriesCollection spData = new XYSeriesCollection();
		spData.addSeries(pressure());
		final XYItemRenderer spRenderer = new StandardXYItemRenderer();
		final NumberAxis spAxis = new NumberAxis("-Cp");
		final XYPlot spPlot = new XYPlot(spData, null, spAxis, spRenderer);
		spPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
		
		//history.something.size()-1 refers to the last item in the arraylist
		String CLift = history.CLift.get(history.CLift.size()-1);
		String CDrag = history.CDrag.get(history.CDrag.size()-1);
		String CMz = history.CMz.get(history.CMz.size()-1);
		
		
		
		//airfoil subplot
		final XYSeriesCollection afData = new XYSeriesCollection();
		afData.addSeries(airfoil());
		final XYItemRenderer afRenderer = new StandardXYItemRenderer();
		final NumberAxis afAxis = new NumberAxis("");
		final XYPlot afPlot = new XYPlot(afData, null, afAxis, afRenderer);
		Range range = afPlot.getDataRange(afAxis);
		afAxis.setRange(range);
		afPlot.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
		
		Double axis = (range.getLowerBound() + range.getUpperBound())/2;
		Double axisSpan = (range.getUpperBound() - range.getLowerBound());
		
		afPlot.addAnnotation(new XYTextAnnotation("CLift: " + CLift, /**/ 0.25, axis + (axisSpan/4)));
		afPlot.addAnnotation(new XYTextAnnotation("CDrag: " + CDrag, /**/ 0.25, axis));
		afPlot.addAnnotation(new XYTextAnnotation("CMz: " + CMz,     /**/ 0.25, axis - (axisSpan/4)));
		//parent plot
		final NumberAxis domainAxis = new NumberAxis("Airfoil");
		final CombinedDomainXYPlot plot = new CombinedDomainXYPlot(domainAxis);
		plot.setGap(10.0);
		
		//add subplots
		plot.add(spPlot, 1);
		plot.add(afPlot, 1);
		plot.setOrientation(PlotOrientation.VERTICAL);
		
		
		
		//style
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setBaseShapesVisible(false);
		renderer.setSeriesPaint(0, Color.BLACK);
		plot.setRenderer(renderer);
		plot.setBackgroundPaint(Color.WHITE);
		plot.setDomainGridlinePaint(Color.BLACK);
		plot.setRangeGridlinePaint(Color.BLACK);
		
		//create parent chart
		JFreeChart chart = new JFreeChart("Surface Pressure", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
		chart.removeLegend();
		ChartPanel chartPanel = new ChartPanel( chart ){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void restoreAutoRangeBounds(){
				//empty body: do not reset y zoom
			}
		};
		chartPanel.setRangeZoomable(false);
		chartPanel.setPreferredSize( new java.awt.Dimension(560,367));
		return chartPanel;
	}
	
	
	//data 
	private XYSeries airfoil(){
		
		final XYSeries a = new XYSeries( "Airfoil", /**connect "left to right"*/ false, /**allow duplicates*/ true);
		
		int dataSize = surfaceFlow.Global_Index.size();
		for ( int i = 0; i < dataSize; i++ ){
			double x_coord = Double.parseDouble(surfaceFlow.x_coord.get(i));
			double y_coord = Double.parseDouble(surfaceFlow.y_coord.get(i));
			a.add( x_coord, y_coord-2 );
		}
		
		return a;
	}
	
	private XYSeries pressure(){
		
		final XYSeries s = new XYSeries( "Pressure", false, true );
		
		int dataSize = surfaceFlow.Global_Index.size();
		for ( int i = 0; i < dataSize; i++ ){
			double x_coord = Double.parseDouble(surfaceFlow.x_coord.get(i));
			double Pressure_Coefficient = Double.parseDouble(surfaceFlow.Pressure_Coefficient.get(i));
			s.add( x_coord, -Pressure_Coefficient );
		}
		
		return s;
	}
}