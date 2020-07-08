import com.google.inject.Guice;
import com.google.inject.Injector;

public class ApplicationEntry {
    public static void main(String[] args) {
        final Injector injector = Guice.createInjector(new ApplicationModule());
        final BackItUp backItUp = injector.getInstance(BackItUp.class);
        backItUp.execute();
    }
}
