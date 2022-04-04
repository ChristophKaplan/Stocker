package testcase;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Set;

import stocker.IStockerTester;
import stocker.model.database.DatabaseModel;
import stocker.model.general.BollingerBands;
import stocker.model.general.DisplayType;
import stocker.model.general.IndicatorType;
import stocker.model.general.SimpleMovingAverage;
import stocker.model.properties.PropertiesModel;


public class MeineTesterKlasse implements IStockerTester {

	PropertiesModel propertiesModel;
	DatabaseModel databaseModel;
	
	public MeineTesterKlasse(){

		this.propertiesModel = new PropertiesModel();
		this.propertiesModel.resetToFactorySettings();
		this.databaseModel = new DatabaseModel();
	}
	

	
	@Override
	public String getMatrNr() {
		// TODO Auto-generated method stub
		return propertiesModel.getMatrNr();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return propertiesModel.getName();
	}

	@Override
	public String getEmail() {
		// TODO Auto-generated method stub
		return propertiesModel.getEmail();
	}

	@Override
	public void clearWatchlist() {
		// TODO Auto-generated method stub
		databaseModel.clearDatabaseByDisplayType(DisplayType.Watchlist);
	}

	@Override
	public void addWatchlistEntry(String stockId) {
		databaseModel.addStock(stockId,DisplayType.Watchlist);
	}

	@Override
	public void removeWatchlistEntry(String stockId) {
		databaseModel.deleteStock(stockId);
	}

	@Override
	public String[] getWatchlistStockIds() {
		ArrayList<String> lst = databaseModel.getSymbolListByDisplayType(DisplayType.Watchlist);
		return lst.toArray(new String[lst.size()]);
	}

	@Override
	public void clearAlarms(String stockId) {
		databaseModel.clearAlarms(stockId);
	}

	@Override
	public void clearAllAlarms() {
		databaseModel.clearAllAlarms();
	}

	@Override
	public void addAlarm(String stockId, double threshold) {
		databaseModel.addAlarm(stockId, threshold);
	}

	@Override
	public void removeAlarm(String stockId, double threshold) {
		databaseModel.removeAlarm(stockId, threshold);
	}

	@Override
	public double[] getAlarms(String stockId) {
		if(!databaseModel.hasAlarm(stockId)){
			return new double[0];
		}
		
		ArrayList<Double> alarmList = databaseModel.getAlarms(stockId);
		double[] result = new double[alarmList.size()];
		for(int i = 0; i<alarmList.size();i++) {
			result[i] = alarmList.get(i);
		}
		return result;
	}

	@Override
	public Set<String> getAlarmStockIds() {
		return databaseModel.getAlarmWrapperMap().keySet();
	}

	
	
	@Override
	public double[] getMovingAverage(int n, double[] stockData) {
		
		SimpleMovingAverage sma = new SimpleMovingAverage(IndicatorType.SimpleMovingAverage,n,Color.red);
		try {
			return sma.getSMA(sma.getMaxAmout(stockData), stockData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	
	@Override
	public double[] getUpperBollingerBand(double f, int n, double[] stockData) {
		BollingerBands bb = new BollingerBands(IndicatorType.BollingerBands,f,n,n,Color.red);
		try {
			return bb.getUpperBollingerBand(bb.getMaxAmout(stockData),stockData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public double[] getLowerBollingerBand(double f, int n, double[] stockData) {
		BollingerBands bb = new BollingerBands(IndicatorType.BollingerBands,f,n,n,Color.red);
		try {
			return bb.getLowerBollingerBand(bb.getMaxAmout(stockData),stockData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}




}
