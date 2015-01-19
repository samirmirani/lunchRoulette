import play.Application;
import play.GlobalSettings;
import play.libs.Akka;
import scala.concurrent.duration.FiniteDuration;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;
import services.RouletteService;
import models.User;

public class Global extends GlobalSettings {
  @Override
  public void onStart(Application app) {
    this.schedulePairing();
    this.scheduleTruncate();
  }

  /**
   * Schedules a job when users are paired with each other.
   */
  public void schedulePairing() {
    //Reset tables at midnight.
    //todo abstract this out.
    Calendar c = Calendar.getInstance();
    c.set(Calendar.HOUR_OF_DAY, 17);
    c.set(Calendar.MINUTE, 5);
    c.set(Calendar.SECOND, 0);
    Date plannedStart = c.getTime();
    Date now = new Date();
    Date nextRun;
    if(now.after(plannedStart)) {
      c.add(Calendar.DAY_OF_WEEK, 1);
      nextRun = c.getTime();
    } else {
      nextRun = c.getTime();
    }

    Long delayInSeconds;

    //this is the delay we need to cron the system to fire at 1030 am every day.
    delayInSeconds = (nextRun.getTime() - now.getTime()) / 1000;

    FiniteDuration delay = FiniteDuration.create(delayInSeconds, TimeUnit.SECONDS);
    FiniteDuration frequency = FiniteDuration.create(24, TimeUnit.HOURS);

    Runnable runPairing = new Runnable() {
      @Override
      public void run() {
        RouletteService.pairingJob();
        System.out.println("Pairning complete");
      }
    };

    Akka.system().scheduler().schedule(delay, frequency, runPairing, Akka.system().dispatcher());
  }

  /**
   * Schedules a job to truncate all tables.
   */
  public void scheduleTruncate() {
    //Reset tables at midnight.
    Calendar c = Calendar.getInstance();
    c.set(Calendar.HOUR_OF_DAY, 24);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    Date plannedStart = c.getTime();
    Date now = new Date();
    Date nextRun;
    if(now.after(plannedStart)) {
      c.add(Calendar.DAY_OF_WEEK, 1);
      nextRun = c.getTime();
    } else {
      nextRun = c.getTime();
    }

    Long delayInSeconds;

    //this is the delay we need to cron the system to fire at 1030 am every day.
    delayInSeconds = (nextRun.getTime() - now.getTime()) / 1000;

    FiniteDuration delay = FiniteDuration.create(delayInSeconds, TimeUnit.SECONDS);
    FiniteDuration frequency = FiniteDuration.create(24, TimeUnit.HOURS);

    Runnable runPairing = new Runnable() {
      @Override
      public void run() {
        User.truncatePairs();
        User.truncateUsers();
        System.out.println("Truncatation Complete....App reset for new day.");
      }
    };

    Akka.system().scheduler().schedule(delay, frequency, runPairing, Akka.system().dispatcher());
  }

}