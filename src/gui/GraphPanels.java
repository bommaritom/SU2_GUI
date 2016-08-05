package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.FileNotFoundException;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
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

import io.SurfaceFlow;

/**
 * This class is designed to easily create multiple panels which
 * can graph various properties of surface_flow.csv
 *
 *
 */
public class GraphPanels{
	
	private SurfaceFlow surfaceFlow;
	
	public ChartPanel sfGraph;
	public CombinedDomainXYPlot plot;
	public XYPlot spPlot;
	public XYSeriesCollection spData;
    
	
	//JFreeChart hierarchy:
	//ChartPanel > CombinedDomainXYPlot > XYPlot > XYSeriesCollection > XYSeries
	
	public GraphPanels() throws FileNotFoundException{
		
		addNewSurfacePressureData();
		
	}
	
	public void addNewSurfacePressureData() throws FileNotFoundException{
		
		surfaceFlow = new SurfaceFlow("su2/surface_flow.csv");
		sfGraph = updateSurfacePressurePanel();
		sfGraph.repaint();
		sfGraph.revalidate();
		
	}
	
	private ChartPanel updateSurfacePressurePanel(){
		
		//surface pressure subplot
		if (spData == null){
			spData = new XYSeriesCollection();
		}
		spData.addSeries(pressureUpper());
		spData.addSeries(pressureLower());
		final XYItemRenderer spRenderer = new StandardXYItemRenderer();
		final NumberAxis spAxis = new NumberAxis("-Cp");
		final XYPlot spPlot = new XYPlot(spData, null, spAxis, spRenderer);
		spPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
		
		
		//airfoil subplot
		final XYSeriesCollection afData = new XYSeriesCollection();
		afData.addSeries(airfoil());
		final XYItemRenderer afRenderer = new StandardXYItemRenderer();
		final NumberAxis afAxis = new NumberAxis("");
		final XYPlot afPlot = new XYPlot(afData, null, afAxis, afRenderer);
		//Range range = afPlot.getDataRange(afAxis);
		Range range = new Range(-.2,.2);
		afAxis.setRange(range);
		afPlot.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
		
		//parent plot
		final NumberAxis domainAxis = new NumberAxis("Upper = Solid - Lower = Dotted");
		final CombinedDomainXYPlot plot = new CombinedDomainXYPlot(domainAxis);
		plot.setGap(10.0);
		
		//add subplots
		plot.add(spPlot, 1);
		plot.add(afPlot, 1);
		plot.setOrientation(PlotOrientation.VERTICAL);
		
		
		
		//style
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setBaseShapesVisible(false);
		for (int i = 0; i < spPlot.getSeriesCount(); i++){
			renderer.setSeriesPaint(i, Color.BLACK);
		}
		/**last two plots (newest) are red*/
		if (spPlot.getSeriesCount() > 2){
			renderer.setSeriesPaint(spPlot.getSeriesCount()-2, Color.RED);
			renderer.setSeriesPaint(spPlot.getSeriesCount()-1, Color.RED);
		}
		for (int i = 1; i < spPlot.getSeriesCount(); i+=2){
			renderer.setSeriesStroke(
				i,
				new BasicStroke(
			        1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
			        10.0f, new float[] {6.0f, 6.0f}, 0.5f
					)		
			);
		}
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
			a.add( x_coord, y_coord );
		}
		
		return a;
	}
	
	private Boolean firstHalfIsUpper(){
		int dataSize = surfaceFlow.Global_Index.size();
		int quarter = Math.round(dataSize/4);
		return (Double.parseDouble(surfaceFlow.y_coord.get(quarter)) > Double.parseDouble(surfaceFlow.y_coord.get(dataSize-quarter)));
	}
	
	private XYSeries pressureUpper(){
		
		final XYSeries s = new XYSeries( "Pressure" + Math.random(), false, true );
		
		int dataSize = surfaceFlow.Global_Index.size();
		
		int start;
		int end;
		if (firstHalfIsUpper()){
			start = 0;
			end = Math.round(dataSize/2);
		} else {
			start = Math.round(dataSize/2);
			end = dataSize;
		}
		
		for ( int i = start; i < end; i++ ){
			double x_coord = Double.parseDouble(surfaceFlow.x_coord.get(i));
			double Pressure_Coefficient = Double.parseDouble(surfaceFlow.Pressure_Coefficient.get(i));
			s.add( x_coord, -Pressure_Coefficient );
		}
		
		return s;
	}
	
	private XYSeries pressureLower(){
		
		final XYSeries s = new XYSeries( "Pressure" + Math.random(), false, true );
		
		int dataSize = surfaceFlow.Global_Index.size();
		
		int start;
		int end;
		if (!firstHalfIsUpper()){
			start = 0;
			end = Math.round(dataSize/2);
		} else {
			start = Math.round(dataSize/2);
			end = dataSize;
		}
		
		for ( int i = start; i < end; i++ ){
			double x_coord = Double.parseDouble(surfaceFlow.x_coord.get(i));
			double Pressure_Coefficient = Double.parseDouble(surfaceFlow.Pressure_Coefficient.get(i));
			s.add( x_coord, -Pressure_Coefficient );
		}
		
		return s;
	}
	
}