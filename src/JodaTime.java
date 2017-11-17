import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import junit.framework.TestCase;


public class JodaTime extends TestCase {

	public void test() {
		
		// start of cal month view
		LocalDate d = new LocalDate().withDayOfMonth(1).withDayOfWeek(Calendar.SUNDAY);

		// end of cal month view
		LocalDate limit = new LocalDate().withDayOfMonth(1).plusMonths(1).minusDays(1).withDayOfWeek(Calendar.SATURDAY).plusDays(1);

	}
}
