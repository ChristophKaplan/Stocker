package testcase;
import org.junit.jupiter.api.BeforeEach;
import test.StockerTest;

public class StockerTest_3285766_Kaplan_Christoph extends StockerTest{
		
	@Override
	@BeforeEach
	public void setUp() {

		setStockerTester(new MeineTesterKlasse());
	}
}
