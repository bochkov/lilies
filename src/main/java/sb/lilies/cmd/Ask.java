package sb.lilies.cmd;

import java.util.Scanner;
import javax.sql.DataSource;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class Ask implements Execute {

    private final Metadata md;
    private final DataSource ds;
    private final Execute origin;

    @SuppressWarnings("java:S106")
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
