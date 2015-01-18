import play.Application;
import play.GlobalSettings;
import play.libs.Akka;
import scala.concurrent.duration.FiniteDuration;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;
import services.RouletteService;

public class Global extends GlobalSettings {
  @Override
  public void onStart(Application app) {


    //We want to do the pairing at 10:30am PST.

    Calendar c = Calendar.getInstance();
    c.set(Calendar.HOUR_OF_DAY, 10);
    c.set(Calendar.MINUTE, 30);
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

    FiniteDuration delay = FiniteDuration.create(0, TimeUnit.SECONDS);
    FiniteDuration frequency = FiniteDuration.create(5, TimeUnit.SECONDS);

    Runnable runPairing = new Runnable() {
      @Override
      public void run() {
        RouletteService.pairingJob();
        System.out.println("Time is now: " + new Date());
      }
    };

    Akka.system().scheduler().schedule(delay, frequency, runPairing, Akka.system().dispatcher());
  }

}