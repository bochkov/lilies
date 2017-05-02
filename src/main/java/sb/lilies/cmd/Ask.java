package sb.lilies.cmd;

import javax.sql.DataSource;
import java.util.Scanner;

public final class Ask implements Execute {

    private final Execute origin;
    private final Metadata md;
    private final DataSource ds;

    public Ask(Metadata md, DataSource ds, Execute origin) {
        this.origin = origin;
        this.ds = ds;
        this.md = md;
    }

    @Override
    public void act() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Will be created:");
            md.print(ds, System.out);
            System.out.print("Continue (Y/n): ");
            String answer = scanner.next();
            if (answer.equals("Y"))
                this.origin.act();
            else
                System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
